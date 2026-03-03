import '@testing-library/jest-dom';
import { vi } from 'vitest';

// Mocking globally used APIs or browser features
global.ResizeObserver = vi.fn().mockImplementation(() => ({
    observe: vi.fn(),
    unobserve: vi.fn(),
    disconnect: vi.fn(),
}));

// Mocking modules that might cause issues in test environment
vi.mock('lucide-react', () => ({
    ChevronLeft: () => <div data-testid="chevron-left" />,
    ChevronRight: () => <div data-testid="chevron-right" />,
    LayoutGrid: () => <div data-testid="layout-grid" />,
    Users: () => <div data-testid="users" />,
    Newspaper: () => <div data-testid="newspaper" />,
    Settings: () => <div data-testid="settings" />,
    LogOut: () => <div data-testid="logout" />,
}));
