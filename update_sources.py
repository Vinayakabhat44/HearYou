import json
import os

path = r'd:\Vinayak\Personal_Project\Antigravity\mitra-core-service\src\main\resources\sources.json'
new_sources = [
    { "name": "Times of India - Mumbai", "url": "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Delhi", "url": "https://timesofindia.indiatimes.com/rssfeeds/1081479906.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Hyderabad", "url": "https://timesofindia.indiatimes.com/rssfeeds/5880659.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Chennai", "url": "https://timesofindia.indiatimes.com/rssfeeds/295030.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Kolkata", "url": "https://timesofindia.indiatimes.com/rssfeeds/2128830743.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Pune", "url": "https://timesofindia.indiatimes.com/rssfeeds/-2128816762.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Ahmedabad", "url": "https://timesofindia.indiatimes.com/rssfeeds/-2128821151.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Chandigarh", "url": "https://timesofindia.indiatimes.com/rssfeeds/3947040.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Lucknow", "url": "https://timesofindia.indiatimes.com/rssfeeds/2128822531.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Patna", "url": "https://timesofindia.indiatimes.com/rssfeeds/2128817995.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Jaipur", "url": "https://timesofindia.indiatimes.com/rssfeeds/301252.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Nagpur", "url": "https://timesofindia.indiatimes.com/rssfeeds/446254.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Rajkot", "url": "https://timesofindia.indiatimes.com/rssfeeds/3947029.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Ranchi", "url": "https://timesofindia.indiatimes.com/rssfeeds/446253.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Surat", "url": "https://timesofindia.indiatimes.com/rssfeeds/3947026.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Vadodara", "url": "https://timesofindia.indiatimes.com/rssfeeds/3947021.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Varanasi", "url": "https://timesofindia.indiatimes.com/rssfeeds/3947058.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Thane", "url": "https://timesofindia.indiatimes.com/rssfeeds/1015697525.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Times of India - Thiruvananthapuram", "url": "https://timesofindia.indiatimes.com/rssfeeds/3942006.cms", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Indian Express - Home", "url": "https://indianexpress.com/feed/", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Indian Express - Technology", "url": "https://indianexpress.com/section/technology/feed/", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Indian Express - Entertainment", "url": "https://indianexpress.com/section/entertainment/feed/", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Indian Express - Sports", "url": "https://indianexpress.com/section/sports/feed/", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "NDTV - Top Stories", "url": "https://feeds.feedburner.com/ndtvnews-top-stories", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "NDTV - India News", "url": "https://feeds.feedburner.com/ndtvnews-india-news", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "NDTV - Profit (Business)", "url": "https://feeds.feedburner.com/ndtvprofit-latest", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "India Today - Home", "url": "https://www.indiatoday.in/rss/home", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "News18 - India", "url": "https://www.news18.com/rss/india.xml", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "OneIndia - Kannada", "url": "https://kannada.oneindia.com/rss/kannada-news-fb.xml", "type": "RSS", "language": "kn", "region": "India", "active": True },
    { "name": "OneIndia - Hindi", "url": "https://hindi.oneindia.com/rss/hindi-news-fb.xml", "type": "RSS", "language": "hi", "region": "India", "active": True },
    { "name": "OneIndia - Telugu", "url": "https://telugu.oneindia.com/rss/telugu-news-fb.xml", "type": "RSS", "language": "te", "region": "India", "active": True },
    { "name": "OneIndia - Tamil", "url": "https://tamil.oneindia.com/rss/tamil-news-fb.xml", "type": "RSS", "language": "ta", "region": "India", "active": True },
    { "name": "OneIndia - Malayalam", "url": "https://malayalam.oneindia.com/rss/malayalam-news-fb.xml", "type": "RSS", "language": "ml", "region": "India", "active": True },
    { "name": "OneIndia - Gujarati", "url": "https://gujarati.oneindia.com/rss/gujarati-news-fb.xml", "type": "RSS", "language": "gu", "region": "India", "active": True },
    { "name": "OneIndia - Bengali", "url": "https://bengali.oneindia.com/rss/bengali-news-fb.xml", "type": "RSS", "language": "bn", "region": "India", "active": True },
    { "name": "OneIndia - Marathi", "url": "https://marathi.oneindia.com/rss/marathi-news-fb.xml", "type": "RSS", "language": "mr", "region": "India", "active": True },
    { "name": "Vishwa Vani - Home - Kannada", "url": "https://vishwavani.news/feed/", "type": "RSS", "language": "kn", "region": "India", "active": True },
    { "name": "Kannada Prabha - Home - Kannada", "url": "https://www.kannadaprabha.com/rss/home.xml", "type": "RSS", "language": "kn", "region": "India", "active": True },
    { "name": "Udayavani - Home - Kannada", "url": "https://www.udayavani.com/rss/home.xml", "type": "RSS", "language": "kn", "region": "India", "active": True },
    { "name": "ABP Live - India - Hindi", "url": "https://www.abplive.com/home/feed", "type": "RSS", "language": "hi", "region": "India", "active": True },
    { "name": "ABP Ananda - Home - Bengali", "url": "https://bengali.abplive.com/home/feed", "type": "RSS", "language": "bn", "region": "India", "active": True },
    { "name": "ABP Majha - Home - Marathi", "url": "https://marathi.abplive.com/home/feed", "type": "RSS", "language": "mr", "region": "India", "active": True },
    { "name": "The Hindu - National", "url": "https://www.thehindu.com/news/national/?service=rss", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "The Hindu - International", "url": "https://www.thehindu.com/news/international/?service=rss", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "The Hindu - Business", "url": "https://www.thehindu.com/business/?service=rss", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "The Hindu - Sport", "url": "https://www.thehindu.com/sport/?service=rss", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Deccan Herald - National", "url": "https://www.deccanherald.com/national/rss.xml", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Deccan Herald - World", "url": "https://www.deccanherald.com/international/rss.xml", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Deccan Herald - Business", "url": "https://www.deccanherald.com/business/rss.xml", "type": "RSS", "language": "en", "region": "India", "active": True },
    { "name": "Varthabharathi - Home - Kannada", "url": "https://www.varthabharati.in/rss.xml", "type": "RSS", "language": "kn", "region": "India", "active": True },
    { "name": "Jag Bani - Home - Punjabi", "url": "https://punjabi.jagbani.com/rss/home.xml", "type": "RSS", "language": "pa", "region": "India", "active": True },
    { "name": "Lokmat - National - Marathi", "url": "https://www.lokmat.com/rss/national.xml", "type": "RSS", "language": "mr", "region": "India", "active": True },
]

try:
    with open(path, 'r', encoding='utf-8') as f:
        sources = json.load(f)
    
    existing_urls = {s['url'] for s in sources}
    added_count = 0
    for ns in new_sources:
        if ns['url'] not in existing_urls:
            sources.append(ns)
            added_count += 1
            
    with open(path, 'w', encoding='utf-8') as f:
        json.dump(sources, f, indent=4, ensure_ascii=False)
    print(f"Successfully added {added_count} new sources.")
except Exception as e:
    print(f"Error: {e}")
