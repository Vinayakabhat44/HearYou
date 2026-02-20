# Antigravity-UI Story: Part 3 - The Global Brain 🧠

In a complex app, different components need to share data. The most important data in Antigravity is the **User**. This is managed by the `AuthContext`.

## 1. The Concept: Context API

Instead of passing user data down from `App` -> `MainLayout` -> `Sidebar` -> `Avatar`, we use the **Context API**. This creates a "data cloud" that any component can tap into.

```javascript
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  
  // Logic to login/logout/etc.
  
  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
```

## 2. The Custom Hook: `useAuth`

We don't want to type `useContext(AuthContext)` every time. So we created a custom hook:

```javascript
export const useAuth = () => useContext(AuthContext);
```

### ⚛️ React Concept: Custom Hooks
Hooks like `useAuth` allow you to extract component logic into reusable functions. Now, any component can just say `const { user } = useAuth();` and it works!

## 3. Persistence with `useEffect`

When you refresh the page, you don't want to be logged out. `AuthContext` uses a `useEffect` hook to check for a saved user session when the app starts.

```javascript
useEffect(() => {
  const savedUser = authService.getCurrentUser();
  if (savedUser) {
    setUser(savedUser);
  }
  setLoading(false);
}, []);
```

### ⚛️ React Concept: `useEffect` Dependency Array
The empty brackets `[]` mean this effect runs **only once**, when the component first mounts. It's the perfect place for initialization logic.

---
*Next in Part 4: Bringing it to Life—Story Feeds and API Calls.*
