import React, { useRef } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import './TabScroll.css';

const TabScroll = ({ tabs, activeTab, onTabChange }) => {
    const scrollRef = useRef(null);

    const scroll = (direction) => {
        if (scrollRef.current) {
            const scrollAmount = 200;
            scrollRef.current.scrollBy({
                left: direction === 'left' ? -scrollAmount : scrollAmount,
                behavior: 'smooth'
            });
        }
    };

    return (
        <div className="tab-scroll-wrapper glass-panel">
            <button className="scroll-arrow left" onClick={() => scroll('left')}>
                <ChevronLeft size={20} />
            </button>

            <div className="tabs-scroll" ref={scrollRef}>
                {tabs.map(tab => (
                    <button
                        key={tab}
                        className={`tab-item ${activeTab === tab ? 'active' : ''}`}
                        onClick={() => onTabChange(tab)}
                    >
                        {tab}
                    </button>
                ))}
            </div>

            <button className="scroll-arrow right" onClick={() => scroll('right')}>
                <ChevronRight size={20} />
            </button>
        </div>
    );
};

export default TabScroll;
