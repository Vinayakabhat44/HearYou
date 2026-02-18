import React from 'react';
import './Input.css';

const Input = ({ label, type = 'text', placeholder, value, onChange, name, required = false, error }) => {
    return (
        <div className="input-group">
            {label && <label className="input-label">{label}</label>}
            <input
                type={type}
                name={name}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                required={required}
                className={`input-field ${error ? 'input-error' : ''}`}
            />
            {error && <span className="error-text">{error}</span>}
        </div>
    );
};

export default Input;
