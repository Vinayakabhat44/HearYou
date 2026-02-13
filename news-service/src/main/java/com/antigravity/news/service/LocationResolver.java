package com.antigravity.news.service;

import com.antigravity.news.entity.NewsArticleEntity;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

@Service
public class LocationResolver {

    private static final Map<String, String> DISTRICT_MAP = new HashMap<>();
    static {
        DISTRICT_MAP.put("Bangalore", "Bangalore Urban");
        DISTRICT_MAP.put("Mysuru", "Mysuru");
        DISTRICT_MAP.put("Belagavi", "Belagavi");
        DISTRICT_MAP.put("Hubballi", "Dharwad");
        DISTRICT_MAP.put("Mangaluru", "Dakshina Kannada");
        DISTRICT_MAP.put("Udupi", "Udupi");
    }

    public void resolveAndTag(NewsArticleEntity article) {
        String text = (article.getTitle() + " " + article.getSummary() + " " + article.getCategory()).toLowerCase();

        // 1. Pincode Extraction
        Pattern pincodePattern = Pattern.compile("\\b(\\d{6})\\b");
        var matcher = pincodePattern.matcher(text);
        if (matcher.find()) {
            article.setPincode(matcher.group(1));
        }

        // 2. District Extraction
        for (Map.Entry<String, String> entry : DISTRICT_MAP.entrySet()) {
            if (text.contains(entry.getKey().toLowerCase())) {
                article.setDistrict(entry.getValue());
                article.setState("Karnataka");
                break;
            }
        }

        // 3. Category Normalization & Extraction
        assignCategory(article, text);

        // 4. Country Fallback
        if (article.getCountry() == null) {
            if (text.contains("international") || text.contains("world") || text.contains("global")
                    || text.contains("usa") || text.contains("uk")) {
                article.setCountry("International");
                article.setCategory("International"); // Force category
            } else {
                article.setCountry("India"); // Default to India
                if (article.getCategory() == null) {
                    article.setCategory("National");
                }
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
