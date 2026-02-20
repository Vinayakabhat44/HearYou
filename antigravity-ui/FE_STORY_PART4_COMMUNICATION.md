# Antigravity-UI Story: Part 4 - Bringing it to Life 🌊

The final piece of our story is how we actually fetch and display data. Let's look at the `Stories` page.

## 1. Data Fetching with `Axios`

We use **Axios** for all our API calls. It's more powerful than the standard `fetch` API, allowing for easy interceptors and better error handling.

Our `feedService` abstracts these calls:
```javascript
// feedService.js
export const getHierarchicalFeed = async () => {
  const response = await axios.get('/api/feed/hierarchical');
  return response.data;
};
```

## 2. Managing Page State

The `Stories.jsx` page manages its own state for the feed items and the active tab (Local, District, etc.).

```javascript
const [activeTab, setActiveTab] = useState('Local');
const [items, setItems] = useState([]);
```

## 3. The Lifecycle of a Feed

When you change a tab, we need to fetch new data. We do this by adding `activeTab` to our `useEffect` dependency array.

```javascript
useEffect(() => {
  fetchFeed();
}, [activeTab]); // Every time activeTab changes, this runs!
```

### ⚛️ React Concept: Conditional Rendering
We don't just show a blank screen while waiting for data. We use conditional rendering to show a loading state or a "no stories" message.

```javascript
{loading ? (
  <LoadingSpinner />
) : items.length > 0 ? (
  <FeedList items={items} />
) : (
  <EmptyState />
)}
```

## 4. The "Mitra" Philosophy

You'll notice components like `TabScroll`. This is designed to be mobile-friendly and accessible, following the "Mitra" philosophy of making technology easy for everyone, especially seniors.

---

### 🎉 Summary of Libraries Used:
1. **React 19**: The foundation of our UI.
2. **React Router**: For seamless navigation.
3. **Axios**: Our bridge to the backend microservices.
4. **Lucide React**: Beautiful, consistent iconography.
5. **Elastic APM**: Monitoring our performance in "real-time".

*You've now completed the story of Antigravity-UI! You've learned about entry points, layouts, global state, and data fetching.*
