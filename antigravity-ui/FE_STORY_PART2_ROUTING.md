# Antigravity-UI Story: Part 2 - The Skeleton 🏗️

Now that the app is booted, we need a skeleton to hold it together. In React, we call this a **Layout**.

## 1. The Frame: `MainLayout.jsx`

If you look at Antigravity when logged in, you see a sidebar on the left, ads on the right, and content in the middle. This isn't magic—it's `MainLayout`.

```javascript
const MainLayout = ({ children }) => {
  return (
    <div className="main-layout">
      <nav className="left-sidebar">...</nav>
      <main className="center-content">{children}</main>
      <aside className="right-sidebar">...</aside>
    </div>
  );
};
```

### ⚛️ React Concept: `children`
The `{children}` prop is one of the most powerful features of React. It allows `MainLayout` to act as a wrapper. Any component put inside `<MainLayout>...</MainLayout>` will appear in that `{children}` slot!

## 2. Navigation with `NavLink`

Inside the sidebar, we use `NavLink` from `react-router-dom`. 

```javascript
<NavLink to="/feed" className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}>
  <LayoutGrid size={24} />
  <span>Stories</span>
</NavLink>
```

- **Why `NavLink`?**: It's smarter than a standard link. It automatically knows if it matches the current URL and can apply an `active` class. No manual state management needed!

## 3. Beautiful Icons with `lucide-react`

We use the **Lucide** library for our icons. They are SVG-based, lightweight, and very easy to use as React components.

```javascript
import { LayoutGrid, Users, Newspaper } from 'lucide-react';
```

## 4. The "Glassmorphism" Design

You might notice the `glass-panel` class everywhere. This is part of our **Premium Design System**. It uses backdrop blurs and subtle transparency to give the app a modern, high-end feel.

```css
.glass-panel {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}
```

---
*Next in Part 3: The Secret Sauce—Global State and AuthContext.*
