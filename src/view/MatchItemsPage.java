package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// This page lets staff manually match a lost item with a found item
// It shows two lists side by side and calculates a match score when staff selects one from each
public class MatchItemsPage {

    // stage is the main window, staffName tracks who is logged in
    // Both are private - encapsulation keeps them safe from outside access
    private Stage stage;
    private String staffName;

    // These two lists hold all lost and found items loaded from the database
    private List<Item> lostItems;
    private List<Item> foundItems;

    // ToggleGroups make sure only one radio button can be selected at a time in each list
    private ToggleGroup lostGroup;
    private ToggleGroup foundGroup;

    // Constructor - gets the window and staff name, then loads data and builds the page
    public MatchItemsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
        loadItems();  // fetch items from DB first
        buildPage();  // then build the UI
    }

    // Loads both lost and found item lists from the database
    // Abstraction: the caller just says "load items" without worrying about the SQL details
    private void loadItems() {
        lostItems  = getItemsByType("Lost");
        foundItems = getItemsByType("Found");
    }

    // Fetches items from the database filtered by type ("Lost" or "Found")
    // Returns a list of Item objects built from the query results
    // Encapsulation: the SQL logic is hidden in here, other methods just use the returned list
    private List<Item> getItemsByType(String type) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT id, name, description FROM items WHERE type = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            // For each row returned, create an Item object and add it to the list
            while (rs.next()) {
                list.add(new Item(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Sets up the full page layout with the sidebar and main content
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // Reuse the sidebar from StaffDashboardPage - no need to rewrite it here
        // This is reuse/composition: we borrow just the sidebar from another class
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Match Items"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Match Items");
        stage.setScene(scene);
    }

    // Builds the center section: title, subtitle, and the 3-column match area
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Match Items");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Select one lost item and one found item to match.");
        subtitle.setTextFill(Color.GRAY);

        // The 3 columns: lost items list, found items list, and match result box
        HBox matchArea = new HBox(20);

        // Build each list box using the same method - just passing different data
        // Abstraction: buildItemListBox() handles both lists, we just tell it what to show
        VBox lostBox  = buildItemListBox("Lost Items",  lostItems,  true);
        VBox foundBox = buildItemListBox("Found Items", foundItems, false);

        // Right side box that shows the match result after clicking the button
        VBox matchResultBox = new VBox(10);
        matchResultBox.setPrefWidth(250);
        matchResultBox.setPadding(new Insets(20));
        matchResultBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label matchTitle = new Label("Match Result");
        matchTitle.setStyle("-fx-font-size:15px; -fx-font-weight:bold;");

        Label matchInfo = new Label("Select one item from each list and click Match.");
        matchInfo.setWrapText(true);
        matchInfo.setTextFill(Color.GRAY);

        // This label updates to show the percentage score after matching
        Label matchScore = new Label();

        Button matchBtn = new Button("Match Selected");
        matchBtn.setPrefWidth(200);
        matchBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-background-radius:8; -fx-padding:10;");

        // When Match is clicked, get the selected items and calculate how similar they are
        matchBtn.setOnAction(_ -> {
            RadioButton selectedLost  = (RadioButton) lostGroup.getSelectedToggle();
            RadioButton selectedFound = (RadioButton) foundGroup.getSelectedToggle();

            // Make sure the staff actually selected one from each list
            if (selectedLost == null || selectedFound == null) {
                matchScore.setText("Select items to match.");
                matchScore.setTextFill(Color.GRAY);
                return;
            }

            // getUserData() gives us back the index we stored earlier
            // We use it to find the actual Item object from the list
            Item lostItem  = lostItems.get(Integer.parseInt(selectedLost.getUserData().toString()));
            Item foundItem = foundItems.get(Integer.parseInt(selectedFound.getUserData().toString()));

            // Calculate and show the match percentage
            // Green if 70% or above, orange if below - gives a quick visual signal
            int percent = calculateMatchPercentage(lostItem, foundItem);
            matchScore.setText("Match Score: " + percent + "%");
            matchScore.setTextFill(percent >= 70 ? Color.GREEN : Color.ORANGE);
        });

        matchResultBox.getChildren().addAll(matchTitle, matchInfo, matchScore, matchBtn);
        matchArea.getChildren().addAll(lostBox, foundBox, matchResultBox);

        content.getChildren().addAll(title, subtitle, matchArea);
        return content;
    }

    // Builds a white list box with a heading and radio buttons for each item
    // The isLost flag tells us whether to assign the group to lostGroup or foundGroup
    // Abstraction: one method handles both the lost and found list boxes
    private VBox buildItemListBox(String heading, List<Item> items, boolean isLost) {
        VBox box = new VBox(8);
        box.setPrefWidth(300);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label title = new Label(heading);
        title.setStyle("-fx-font-size:15px; -fx-font-weight:bold;");

        // Create a toggle group and assign it to the right field based on which list this is
        ToggleGroup group = new ToggleGroup();
        if (isLost) lostGroup  = group;
        else        foundGroup = group;

        VBox itemList = new VBox(5);

        if (items.isEmpty()) {
            // No items in DB for this type - show a placeholder message
            Label noData = new Label("No items found.");
            noData.setTextFill(Color.GRAY);
            itemList.getChildren().add(noData);
        } else {
            // Create one radio button per item and store its index as user data
            // We store the index so we can look up the Item object when the button is selected
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                RadioButton rb = new RadioButton(item.name);
                rb.setUserData(i); // save position in list for later retrieval
                rb.setToggleGroup(group);
                itemList.getChildren().add(rb);
            }
        }

        box.getChildren().addAll(title, new Separator(), itemList);
        return box;
    }

    // Compares a lost and found item and returns a match percentage
    // Right now it checks if names are exactly the same - 100% if yes, 50% if no
    // This method can be improved later with better matching logic
    private int calculateMatchPercentage(Item lost, Item found) {
        String lostName  = lost.name.toLowerCase();
        String foundName = found.name.toLowerCase();

        // Exact name match gets full score, anything else gets 50%
        int score = lostName.equals(foundName) ? 100 : 50;
        return score;
    }

    // Shows the page on screen in maximized mode
    public void show() {
        stage.setMaximized(true);
        stage.show();
    }

    // Simple inner class to hold item data fetched from the database
    // Kept private and static - it only exists to serve this page, nothing else needs it
    // Encapsulation: this class is hidden inside MatchItemsPage, not exposed to the outside
    private static class Item {
        int id;
        String name;
        String description;

        // Constructor just sets the 3 fields we care about
        Item(int id, String name, String description) {
            this.id          = id;
            this.name        = name;
            this.description = description;
        }
    }
}