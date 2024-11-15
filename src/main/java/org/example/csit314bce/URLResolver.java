package org.example.csit314bce;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class URLResolver {
    @GetMapping({"/", "/InfinityNetwork/login"})
    public String home() {
        return "/InfinityNetwork/SignInAccountUI"; // Resolve to src/main/resources/templates/InfinityNetwork/Login.html
    }

    @GetMapping("/InfinityNetwork/admin/dashboard")
    public String dashboard() {
        return "InfinityNetwork/admin/AdminAccountsUI"; // Resolve to src/main/resources/templates/InfinityNetwork/admin/dashboard.html
    }

    @GetMapping("/InfinityNetwork/admin/profiles")
    public String viewProfiles() {
        return "InfinityNetwork/admin/AdminProfilesUI";
    }

    @GetMapping("InfinityNetwork/admin/create")
    public String createUser() {
        return "InfinityNetwork/admin/CreateUserAccountUI";
    }

    @GetMapping("InfinityNetwork/admin/update")
    public String updateUser(@RequestParam String username, Model model) {
        System.out.println(username);
        // Pass data to the page
        model.addAttribute("username", username);
        return "InfinityNetwork/admin/AdminUpdateUserUI";
    }

    @GetMapping("InfinityNetwork/admin/createProfile")
    public String createProfile() {
        return "InfinityNetwork/admin/CreateUserProfileUI";
    }

    @GetMapping("InfinityNetwork/admin/updateProfile")
    public String updateProfile(@RequestParam String profileName, Model model) {
        System.out.println(profileName);
        // Pass data to the page
        model.addAttribute("profileName", profileName);
        return "InfinityNetwork/admin/updateProfileUI";
    }

    // AGENT

    @GetMapping("InfinityNetwork/agent/dashboard")
    public String agentDashboard() {
        return "InfinityNetwork/agent/dashboard";
    }

    @GetMapping("InfinityNetwork/agent/listings")
    public String agentListings() {
        return "InfinityNetwork/agent/UsedCarAgentListingsUI";
    }

    @GetMapping("InfinityNetwork/agent/create")
    public String agentCreateListing() {
        return "InfinityNetwork/agent/AgentCreateListingUI";
    }

    @GetMapping("InfinityNetwork/agent/reviews")
    public String agentReviews() {
        return "InfinityNetwork/agent/AgentRatingUI";
    }

    @GetMapping("InfinityNetwork/agent/edit")
    public String editAccountInfo() {
        return "InfinityNetwork/agent/AgentChangeDetailsUI";
    }

    @GetMapping("InfinityNetwork/agent/update")
    public String updateListing(@RequestParam String carPlateNumber, Model model) {
        System.out.println(carPlateNumber);
        // Pass data to the page
        model.addAttribute("carPlateNumber", carPlateNumber);
        return "InfinityNetwork/agent/AgentUpdateListingUI";
    }

    @GetMapping("InfinityNetwork/agent/account")
    public String account() {
        return "InfinityNetwork/agent/account";
    }

    // Seller
    @GetMapping("InfinityNetwork/seller/dashboard")
    public String sellerDashboard() {
        return "InfinityNetwork/seller/dashboard";
    }

    @GetMapping("InfinityNetwork/seller/account")
    public String sellerAccount() {
        return "InfinityNetwork/seller/account";
    }

    @GetMapping("InfinityNetwork/seller/edit")
    public String sellerEditAccountInfo() {
        return "InfinityNetwork/seller/SellerChangeDetailsUI";
    }

    @GetMapping("InfinityNetwork/seller/rate")
    public String sellerRateAgent() {
        return "InfinityNetwork/seller/SellerRateAndReviewUI";
    }

    @GetMapping("InfinityNetwork/seller/interests")
    public String sellerInterests() {
        return "InfinityNetwork/seller/SellerTrackInterestsUI";
    }

    //Buyer
    @GetMapping("InfinityNetwork/buyer/dashboard")
    public String buyerDashboard() {
        return "InfinityNetwork/buyer/dashboard";
    }

    @GetMapping("InfinityNetwork/buyer/listings")
    public String buyerListings() {
        return "InfinityNetwork/buyer/BuyerUsedCarListingsUI";
    }


    @GetMapping("InfinityNetwork/buyer/rate")
    public String buyerRateAgent() {
        return "InfinityNetwork/buyer/BuyerRateAndReviewUI";
    }

    @GetMapping("InfinityNetwork/buyer/edit")
    public String buyerEditAccountInfo() {
        return "InfinityNetwork/buyer/BuyerChangeDetailsUI";
    }

    @GetMapping("InfinityNetwork/buyer/account")
    public String buyerAccount() {
        return "InfinityNetwork/buyer/account";
    }

    @GetMapping("InfinityNetwork/buyer/viewUsedCar")
    public String viewListing(@RequestParam String carPlateNumber, Model model) {
        System.out.println(carPlateNumber);
        // Pass data to the page
        model.addAttribute("carPlateNumber", carPlateNumber); 
        return "InfinityNetwork/buyer/BuyerViewUsedCarUI";
    }
    @GetMapping("InfinityNetwork/buyer/loanCalculator")
    public String loanCalculator() {
        return "InfinityNetwork/buyer/BuyerLoanCalcUI";
    }

    @GetMapping("InfinityNetwork/buyer/favorite")
    public String buyerFavorite() {
        return "InfinityNetwork/buyer/BuyerFavoriteUI";
    }

}
