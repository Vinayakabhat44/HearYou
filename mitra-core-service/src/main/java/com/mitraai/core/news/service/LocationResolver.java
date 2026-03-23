package com.mitraai.core.news.service;

import com.mitraai.core.news.entity.NewsArticleEntity;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

@Service
public class LocationResolver {

    private static final Map<String, String> DISTRICT_MAP = new HashMap<>();
    private static final Map<String, String> TALUK_MAP = new HashMap<>();

    static {
        // District Mapping (Standardized names from OSM/Nominatim)
        DISTRICT_MAP.put("bangalore", "Bengaluru Urban");
        DISTRICT_MAP.put("bengaluru", "Bengaluru Urban");
        DISTRICT_MAP.put("ಬೆಂಗಳೂರು", "Bengaluru Urban");
        
        DISTRICT_MAP.put("mysore", "Mysuru");
        DISTRICT_MAP.put("mysuru", "Mysuru");
        DISTRICT_MAP.put("ಮೈಸೂರು", "Mysuru");
        
        DISTRICT_MAP.put("belgaum", "Belagavi");
        DISTRICT_MAP.put("belagavi", "Belagavi");
        DISTRICT_MAP.put("ಬೆಳಗಾವಿ", "Belagavi");
        
        DISTRICT_MAP.put("mangalore", "Dakshina Kannada");
        DISTRICT_MAP.put("mangaluru", "Dakshina Kannada");
        DISTRICT_MAP.put("ಮಂಗಳೂರು", "Dakshina Kannada");
        
        DISTRICT_MAP.put("udupi", "Udupi");
        DISTRICT_MAP.put("ಉಡುಪಿ", "Udupi");

        // Pan-India Major Cities
        DISTRICT_MAP.put("mumbai", "Mumbai");
        DISTRICT_MAP.put("delhi", "Delhi");
        DISTRICT_MAP.put("hyderabad", "Hyderabad");
        DISTRICT_MAP.put("chennai", "Chennai");
        DISTRICT_MAP.put("ahmedabad", "Ahmedabad");
        DISTRICT_MAP.put("pune", "Pune");
        DISTRICT_MAP.put("kolkata", "Kolkata");
        DISTRICT_MAP.put("lucknow", "Lucknow");
        DISTRICT_MAP.put("jaipur", "Jaipur");
        DISTRICT_MAP.put("chandigarh", "Chandigarh");
        DISTRICT_MAP.put("gurgaon", "Gurugram");
        DISTRICT_MAP.put("gurugram", "Gurugram");
        DISTRICT_MAP.put("noida", "Gautam Buddha Nagar");
        DISTRICT_MAP.put("patna", "Patna");
        DISTRICT_MAP.put("nagpur", "Nagpur");
        DISTRICT_MAP.put("indore", "Indore");
        DISTRICT_MAP.put("bhopal", "Bhopal");
        DISTRICT_MAP.put("surat", "Surat");
        DISTRICT_MAP.put("kanpur", "Kanpur");
        DISTRICT_MAP.put("varanasi", "Varanasi");
        DISTRICT_MAP.put("thiruvananthapuram", "Thiruvananthapuram");
        DISTRICT_MAP.put("trivandrum", "Thiruvananthapuram");
        DISTRICT_MAP.put("kochi", "Ernakulam");
        DISTRICT_MAP.put("guwahati", "Kamrup Metropolitan");
        DISTRICT_MAP.put("bhubaneswar", "Khordha");
        DISTRICT_MAP.put("ranchi", "Ranchi");
        DISTRICT_MAP.put("ludhiana", "Ludhiana");
        DISTRICT_MAP.put("amritsar", "Amritsar");
        DISTRICT_MAP.put("shimla", "Shimla");
        DISTRICT_MAP.put("dehradun", "Dehradun");
        DISTRICT_MAP.put("srinagar", "Srinagar");
        DISTRICT_MAP.put("shimoga", "Shivamogga");
        DISTRICT_MAP.put("shivamogga", "Shivamogga");
        DISTRICT_MAP.put("ಶಿವಮೊಗ್ಗ", "Shivamogga");
        
        DISTRICT_MAP.put("hassan", "Hassan");
        DISTRICT_MAP.put("ಹಾಸನ", "Hassan");
        
        DISTRICT_MAP.put("tumkur", "Tumakuru");
        DISTRICT_MAP.put("tumakuru", "Tumakuru");
        DISTRICT_MAP.put("ತುಮಕೂರು", "Tumakuru");
        
        DISTRICT_MAP.put("mandya", "Mandya");
        DISTRICT_MAP.put("ಮಂಡ್ಯ", "Mandya");
        
        DISTRICT_MAP.put("kolar", "Kolar");
        DISTRICT_MAP.put("ಕೋಲಾರ", "Kolar");
        
        DISTRICT_MAP.put("chikkamagaluru", "Chikkamagaluru");
        DISTRICT_MAP.put("ಚಿಕ್ಕಮಗಳೂರು", "Chikkamagaluru");
        
        DISTRICT_MAP.put("davanagere", "Davanagere");
        DISTRICT_MAP.put("ದಾವಣಗೆರೆ", "Davanagere");
        
        DISTRICT_MAP.put("ballari", "Ballari");
        DISTRICT_MAP.put("bellary", "Ballari");
        DISTRICT_MAP.put("ಬಳ್ಳಾರಿ", "Ballari");
        
        DISTRICT_MAP.put("bidar", "Bidar");
        DISTRICT_MAP.put("ಬೀದರ್", "Bidar");
        
        DISTRICT_MAP.put("gulbarga", "Kalaburagi");
        DISTRICT_MAP.put("kalaburagi", "Kalaburagi");
        DISTRICT_MAP.put("ಕಲಬುರಗಿ", "Kalaburagi");
        
        DISTRICT_MAP.put("raichur", "Raichur");
        DISTRICT_MAP.put("ರಾಯಚೂರು", "Raichur");
        
        DISTRICT_MAP.put("koppal", "Koppal");
        DISTRICT_MAP.put("ಕೊಪ್ಪಳ", "Koppal");
        
        DISTRICT_MAP.put("bagalkot", "Bagalkot");
        DISTRICT_MAP.put("ಬಾಗಲಕೋಟೆ", "Bagalkot");
        
        DISTRICT_MAP.put("vijayapura", "Vijayapura");
        DISTRICT_MAP.put("bijapur", "Vijayapura");
        DISTRICT_MAP.put("ವಿಜಯಪುರ", "Vijayapura");

        // Major States Fallback
        DISTRICT_MAP.put("karnataka", "Karnataka State");
        DISTRICT_MAP.put("maharashtra", "Maharashtra State");
        DISTRICT_MAP.put("tamil nadu", "Tamil Nadu State");
        DISTRICT_MAP.put("andhra pradesh", "Andhra Pradesh State");
        DISTRICT_MAP.put("telangana", "Telangana State");
        DISTRICT_MAP.put("kerala", "Kerala State");
        DISTRICT_MAP.put("west bengal", "West Bengal State");
        DISTRICT_MAP.put("uttar pradesh", "Uttar Pradesh State");
        DISTRICT_MAP.put("bihar", "Bihar State");
        DISTRICT_MAP.put("gujarat", "Gujarat State");
        DISTRICT_MAP.put("rajasthan", "Rajasthan State");
        DISTRICT_MAP.put("punjab", "Punjab State");
        DISTRICT_MAP.put("haryana", "Haryana State");
        DISTRICT_MAP.put("madhya pradesh", "Madhya Pradesh State");
        DISTRICT_MAP.put("odisha", "Odisha State");
        DISTRICT_MAP.put("assam", "Assam State");

        // Taluk Mapping (Focus on Bangalore first as requested)
        TALUK_MAP.put("bangalore south", "Bangalore South");
        TALUK_MAP.put("bengaluru south", "Bangalore South");
        TALUK_MAP.put("anekal", "Anekal");
        TALUK_MAP.put("yelahanka", "Yelahanka");
        TALUK_MAP.put("hosakote", "Hosakote");
        TALUK_MAP.put("devanahalli", "Devanahalli");
        TALUK_MAP.put("dodballapur", "Doddaballapura");
    }

    public void resolveAndTag(NewsArticleEntity article) {
        String text = (article.getTitle() + " " + article.getSummary() + " " + article.getCategory()).toLowerCase();

        boolean isIndia = article.getCountry() == null || "India".equalsIgnoreCase(article.getCountry());

        if (isIndia) {
            // 1. Pincode Extraction
            Pattern pincodePattern = Pattern.compile("\\b(\\d{6})\\b");
            var matcher = pincodePattern.matcher(text);
            if (matcher.find()) {
                article.setPincode(matcher.group(1));
            }

            // 2. District Extraction
            for (Map.Entry<String, String> entry : DISTRICT_MAP.entrySet()) {
                if (text.contains(entry.getKey())) {
                    article.setDistrict(entry.getValue());
                    article.setState("Karnataka");
                    break;
                }
            }

            // 3. Taluk Extraction
            for (Map.Entry<String, String> entry : TALUK_MAP.entrySet()) {
                if (text.contains(entry.getKey())) {
                    article.setTaluk(entry.getValue());
                    // If taluk found, set district to Bengaluru Urban for these specific ones
                    if (entry.getKey().contains("bangalore") || entry.getKey().equals("anekal") || entry.getKey().equals("yelahanka")) {
                        article.setDistrict("Bengaluru Urban");
                    }
                    article.setState("Karnataka");
                    break;
                }
            }
        }

        // 3. Category Normalization & Extraction
        assignCategory(article, text);

        // 4. Country Fallback - only if not already set from source region
        if (article.getCountry() == null) {
            article.setCountry("India"); // Shouldn't reach here normally, but safe fallback
        }

        // 5. Force Non-Standard Categories to National/International
        if (article.getCategory() == null || !isStandardCategory(article.getCategory())) {
            if ("India".equalsIgnoreCase(article.getCountry())) {
                article.setCategory("National");
            } else {
                article.setCategory("International");
            }
        }
    }

    private void assignCategory(NewsArticleEntity article, String text) {
        // Map common terms to our standard categories
        if (text.contains("cricket") || text.contains("ipl") || text.contains("bcci")) {
            article.setCategory("Cricket");
        } else if (text.contains("market") || text.contains("finance") || text.contains("economy")
                || text.contains("bank") || text.contains("nifty") || text.contains("sensex")
                || text.contains("business")) {
            article.setCategory("Finance");
        } else if (text.contains("entertainment") || text.contains("cinema") || text.contains("movie")
                || text.contains("bollywood") || text.contains("hollywood") || text.contains("actor")) {
            article.setCategory("Entertainment");
        } else if (text.contains("health") || text.contains("doctor") || text.contains("vaccine")
                || text.contains("virus") || text.contains("hospital")) {
            article.setCategory("Health");
        } else if (text.contains("sport") || text.contains("football") || text.contains("hockey")
                || text.contains("tennis")) {
            article.setCategory("Sports"); // General sports excluding cricket
        }

        // If RSS provided a category, try to standardize it if not already set
        if (article.getCategory() != null && !isStandardCategory(article.getCategory())) {
            String rssCat = article.getCategory().toLowerCase();
            if (rssCat.contains("business"))
                article.setCategory("Finance");
            else if (rssCat.contains("top stories"))
                article.setCategory("National");
            else if (rssCat.contains("nation"))
                article.setCategory("National");
        }
    }

    private boolean isStandardCategory(String cat) {
        return cat.equals("Cricket") || cat.equals("Finance") || cat.equals("Health") ||
                cat.equals("Entertainment") || cat.equals("Sports") || cat.equals("National")
                || cat.equals("International");
    }
}
