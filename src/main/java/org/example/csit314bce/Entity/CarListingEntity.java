package org.example.csit314bce.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class CarListingEntity {
    private String carBrand;
    private String carModel;
    private String carPlateNumber;
    private LocalDateTime created_at;
    private String listingStatus;
    private int manufacturedYear;
    private double millage;
    private String photo;
    private double price;
    private LocalDateTime updated_at;
    private String listedBy;
    private String sellerUsername;
    private int viewCount;
    private int favoriteCount;
    private DataSource dataSource;

    public CarListingEntity() {
    }

    @Autowired
    public CarListingEntity(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public int getManufacturedYear() {
        return manufacturedYear;
    }

    public void setManufacturedYear(int manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public double getMillage() {
        return millage;
    }

    public void setMillage(double millage) {
        this.millage = millage;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public String getListedBy() {
        return listedBy;
    }

    public void setListedBy(String listedBy) {
        this.listedBy = listedBy;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }


    //Functions
    public List<CarListingEntity> fetchAllListing(String username) {
        List<CarListingEntity> listings = new ArrayList<>();
        String sql = "SELECT * FROM carlistings WHERE listedBy = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the username parameter in the SQL query
            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    CarListingEntity listing = new CarListingEntity();
                    listing.setCarBrand(rs.getString("carBrand"));
                    listing.setCarModel(rs.getString("carModel"));
                    listing.setCarPlateNumber(rs.getString("carPlateNumber"));
                    listing.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                    listing.setListingStatus(rs.getString("listingStatus"));
                    listing.setManufacturedYear(rs.getInt("manufacturedYear"));
                    listing.setMillage(rs.getDouble("millage"));
                    listing.setPhoto(rs.getString("photo"));
                    listing.setPrice(rs.getDouble("price"));
                    listing.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                    listing.setListedBy(rs.getString("listedBy"));
                    listing.setSellerUsername(rs.getString("sellerUsername"));
                    listing.setViewCount(rs.getInt("viewCount"));
                    listing.setFavoriteCount(rs.getInt("favoriteCount"));
                    listings.add(listing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    public String createListing(MultipartFile photo, CarListingEntity listing) {
        this.created_at = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        this.updated_at = this.created_at;

        if (photo != null && !photo.isEmpty()) {
            try {
                // Generate a unique filename with the original extension
                String originalFilename = photo.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID().toString() + extension;

                // Define the path to save the photo in src/main/resources/static/img/
                Path imagePath = Paths.get("src", "main", "resources", "static", "img", newFilename).toAbsolutePath().normalize();

                // Create directories if they don't exist
                Files.createDirectories(imagePath.getParent());

                // Save the file locally
                Files.copy(photo.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                // Set the photo path relative to the static directory
                listing.setPhoto("/img/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
                return "error: Failed to save photo.";
            }
        }

        String sql = "INSERT INTO carListings (carBrand, carModel, carPlateNumber, created_At, listingStatus, manufacturedYear, millage, photo, price, updated_at, listedBy, sellerUsername,viewCount, favoriteCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, listing.getCarBrand());
            statement.setString(2, listing.getCarModel());
            statement.setString(3, listing.getCarPlateNumber());
            statement.setTimestamp(4, Timestamp.valueOf(this.created_at));
            statement.setString(5, listing.getListingStatus());
            statement.setInt(6, listing.getManufacturedYear());
            statement.setDouble(7, listing.getMillage());
            statement.setString(8, listing.getPhoto());
            statement.setDouble(9, listing.getPrice());
            statement.setTimestamp(10, Timestamp.valueOf(this.updated_at));
            statement.setString(11, listing.getListedBy());
            statement.setString(12, listing.getSellerUsername());
            statement.setString(13, "0");
            statement.setString(14, "0");

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted == 1) {
                System.out.println("Listing created successfully!");
                return "success";
            } else {
                System.out.println("Failed to create listing.");
                return "failure";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    public String updateListing(MultipartFile photo, CarListingEntity listing, String originalCarPlateNumber) {
        listing.setUpdated_at(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        //Handle photo upload if present
        if (photo != null && !photo.isEmpty()) {
            try {
                // Generate a unique filename with the original extension
                String originalFilename = photo.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID().toString() + extension;

                // Define the path to save the photo in src/main/resources/static/img/
                Path imagePath = Paths.get("src", "main", "resources", "static", "img", newFilename).toAbsolutePath().normalize();

                // Create directories if they don't exist
                Files.createDirectories(imagePath.getParent());

                // Save the file locally
                Files.copy(photo.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                // Set the photo path relative to the static directory
                listing.setPhoto("/img/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
                return "error: Failed to save photo.";
            }
        }

        String sql = "UPDATE carListings SET carBrand = ?, carModel = ?, carPlateNumber = ?, listingStatus = ?, manufacturedYear = ?, millage = ?, photo = ?, price = ?, updated_at = ?, listedBy = ?, sellerUsername = ? WHERE carPlateNumber = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, listing.getCarBrand());
            statement.setString(2, listing.getCarModel());
            statement.setString(3, listing.getCarPlateNumber());
            statement.setString(4, listing.getListingStatus());
            statement.setInt(5, listing.getManufacturedYear());
            statement.setDouble(6, listing.getMillage());
            statement.setString(7, listing.getPhoto());
            statement.setDouble(8, listing.getPrice());
            statement.setTimestamp(9, Timestamp.valueOf(listing.getUpdated_at()));
            statement.setString(10, listing.getListedBy());
            statement.setString(11, listing.getSellerUsername());
            statement.setString(12, originalCarPlateNumber);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 1) {
                System.out.println("Listing updated successfully!");
                return "success";
            } else {
                System.out.println("Failed to update listing. Listing may not exist.");
                return "failure";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    public String deleteListing(String carPlateNumber) {
        String photoPath = null;
        String selectSql = "SELECT photo FROM carListings WHERE carPlateNumber = ?";
        String deleteSql = "DELETE FROM carListings WHERE carPlateNumber = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {

            // Step 1: Retrieve the photo path for the listing
            selectStmt.setString(1, carPlateNumber);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                photoPath = rs.getString("photo");
            } else {
                return "error: Listing not found.";
            }

            // Step 2: Delete the listing from the database
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, carPlateNumber);
                int rowsDeleted = deleteStmt.executeUpdate();

                if (rowsDeleted == 1) {
                    if (photoPath != null && !photoPath.isEmpty()) {
                        // Extract the filename from the photo path
                        String filename = Paths.get(photoPath).getFileName().toString();
                        Path imagePath = Paths.get("src", "main", "resources", "static", "img", filename).toAbsolutePath().normalize();

                        try {
                            Files.deleteIfExists(imagePath);
                            System.out.println("Photo deleted successfully!");
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Optionally, log the error or handle it as needed
                        }
                    }

                    System.out.println("Listing deleted successfully!");
                    return "success";
                } else {
                    System.out.println("Failed to delete listing.");
                    return "failure: Unable to delete listing.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    public List<CarListingEntity> searchListing(String criteria, String value, String username) {
        List<CarListingEntity> listings = new ArrayList<>();

        // Define allowed columns to prevent SQL injection
        List<String> allowedCriteria = Arrays.asList(
                "carBrand",
                "carModel",
                "carPlateNumber",
                "listingStatus",
                "manufacturedYear",
                "sellerUsername"
        );

        if (!allowedCriteria.contains(criteria)) {
            System.out.println("Invalid search criteria: " + criteria);
            return listings; // Returns an empty list if criteria is invalid
        }

        // SQL query using the validated criteria and filtering by listedBy
        String sql = "SELECT carBrand, carModel, carPlateNumber, created_At, listingStatus, " +
                "manufacturedYear, millage, photo, price, updated_at, listedBy, sellerUsername, viewCount, favoriteCount " +
                "FROM carListings WHERE " + criteria + " LIKE ? AND listedBy = ?";

        String likeValue = "%" + value + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Wildcards search for partial matching
            statement.setString(1, likeValue);
            // Set the username parameter
            statement.setString(2, username);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                CarListingEntity listing = new CarListingEntity();
                listing.setCarBrand(rs.getString("carBrand"));
                listing.setCarModel(rs.getString("carModel"));
                listing.setCarPlateNumber(rs.getString("carPlateNumber"));
                listing.setCreated_at(rs.getTimestamp("created_At").toLocalDateTime());
                listing.setListingStatus(rs.getString("listingStatus"));
                listing.setManufacturedYear(rs.getInt("manufacturedYear"));
                listing.setMillage(rs.getDouble("millage"));
                listing.setPhoto(rs.getString("photo"));
                listing.setPrice(rs.getDouble("price"));
                listing.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                listing.setListedBy(rs.getString("listedBy"));
                listing.setSellerUsername(rs.getString("sellerUsername"));
                listing.setViewCount(rs.getInt("viewCount"));
                listing.setFavoriteCount(rs.getInt("favoriteCount"));
                listings.add(listing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listings;
    }

    public List<CarListingEntity> searchListingForBuyer(String criteria, String value) {
        List<CarListingEntity> listings = new ArrayList<>();

        // Define allowed columns to prevent SQL injection
        List<String> allowedCriteria = Arrays.asList(
                "carBrand",
                "carModel",
                "carPlateNumber",
                "listingStatus",
                "manufacturedYear",
                "sellerUsername"
        );

        if (!allowedCriteria.contains(criteria)) {
            System.out.println("Invalid search criteria: " + criteria);
            return listings; // Returns an empty list if criteria is invalid
        }

        // SQL query using the validated criteria and filtering by listedBy
        String sql = "SELECT carBrand, carModel, carPlateNumber, created_At, listingStatus, " +
                "manufacturedYear, millage, photo, price, updated_at, listedBy, sellerUsername, viewCount, favoriteCount " +
                "FROM carListings WHERE " + criteria + " LIKE ? ";

        String likeValue = "%" + value + "%";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Wildcards search for partial matching
            statement.setString(1, likeValue);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                CarListingEntity listing = new CarListingEntity();
                listing.setCarBrand(rs.getString("carBrand"));
                listing.setCarModel(rs.getString("carModel"));
                listing.setCarPlateNumber(rs.getString("carPlateNumber"));
                listing.setCreated_at(rs.getTimestamp("created_At").toLocalDateTime());
                listing.setListingStatus(rs.getString("listingStatus"));
                listing.setManufacturedYear(rs.getInt("manufacturedYear"));
                listing.setMillage(rs.getDouble("millage"));
                listing.setPhoto(rs.getString("photo"));
                listing.setPrice(rs.getDouble("price"));
                listing.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                listing.setListedBy(rs.getString("listedBy"));
                listing.setSellerUsername(rs.getString("sellerUsername"));
                listing.setViewCount(rs.getInt("viewCount"));
                listing.setFavoriteCount(rs.getInt("favoriteCount"));
                listings.add(listing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listings;
    }


    public List<CarListingEntity> fetchAllListingForBuyer() {
        List<CarListingEntity> listings = new ArrayList<>();
        String sql = "SELECT * FROM carlistings";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    CarListingEntity listing = new CarListingEntity();
                    listing.setCarBrand(rs.getString("carBrand"));
                    listing.setCarModel(rs.getString("carModel"));
                    listing.setCarPlateNumber(rs.getString("carPlateNumber"));
                    listing.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                    listing.setListingStatus(rs.getString("listingStatus"));
                    listing.setManufacturedYear(rs.getInt("manufacturedYear"));
                    listing.setMillage(rs.getDouble("millage"));
                    listing.setPhoto(rs.getString("photo"));
                    listing.setPrice(rs.getDouble("price"));
                    listing.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                    listing.setListedBy(rs.getString("listedBy"));
                    listing.setSellerUsername(rs.getString("sellerUsername"));
                    listing.setViewCount(rs.getInt("viewCount"));
                    listing.setFavoriteCount(rs.getInt("favoriteCount"));
                    listings.add(listing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }


    // When a buyer view, record the view
    public String recordView(String carPlateNumber) {
        String sql = "UPDATE carlistings SET viewCount = viewCount + 1 WHERE carPlateNumber = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the carPlateNumber parameter
            statement.setString(1, carPlateNumber);

            // Execute the update
            statement.executeUpdate();

            System.out.println("View Count updated successfully!");
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println("View Count updated Failed!");
            return "failed";
        }
    }


    // Fetch all listings for a seller with view counts
    public List<CarListingEntity> fetchViewCountForSellerListings(String sellerUsername) {
        List<CarListingEntity> sellerListings = new ArrayList<>();
        String sql = "SELECT * FROM carlistings WHERE sellerUsername = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the sellerUsername parameter
            statement.setString(1, sellerUsername);

            // Execute the query and process the result set
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    CarListingEntity listing = new CarListingEntity();
                    listing.setCarBrand(rs.getString("carBrand"));
                    listing.setCarModel(rs.getString("carModel"));
                    listing.setCarPlateNumber(rs.getString("carPlateNumber"));
                    listing.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                    listing.setListingStatus(rs.getString("listingStatus"));
                    listing.setManufacturedYear(rs.getInt("manufacturedYear"));
                    listing.setMillage(rs.getDouble("millage"));
                    listing.setPhoto(rs.getString("photo"));
                    listing.setPrice(rs.getDouble("price"));
                    listing.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
                    listing.setListedBy(rs.getString("listedBy"));
                    listing.setSellerUsername(rs.getString("sellerUsername"));
                    listing.setViewCount(rs.getInt("viewCount"));
                    listing.setFavoriteCount(rs.getInt("favoriteCount"));

                    // Add each listing with its view count to the list
                    sellerListings.add(listing);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sellerListings;
    }


}
