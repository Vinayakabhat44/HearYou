import React, { useState } from 'react';
import Button from './Button';
import Input from './Input';
import './CreateStoryModal.css';

const CreateStoryModal = ({ isOpen, onClose, onSubmit }) => {
    const [content, setContent] = useState('');
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const [type, setType] = useState('POST');

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await onSubmit({ content, type }, file);
            setContent('');
            setFile(null);
            onClose();
        } catch (err) {
            console.error('Failed to create story:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="glass-panel modal-content">
                <div className="modal-header">
                    <h2>Share a Story</h2>
                    <button className="close-btn" onClick={onClose}>&times;</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label className="input-label">Story Type</label>
                        <select
                            value={type}
                            onChange={(e) => setType(e.target.value)}
                            className="input-field"
                        >
                            <option value="POST">Post</option>
                            <option value="STORY">Story</option>
                            <option value="ANNOUNCEMENT">Announcement</option>
                        </select>
                    </div>

                    <div className="input-group">
                        <label className="input-label">Content</label>
                        <textarea
                            className="input-field"
                            placeholder="What's on your mind?"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            rows="4"
                            required
                        />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Media (Image/Video)</label>
                        <input
                            type="file"
                            className="input-field"
                            onChange={(e) => setFile(e.target.files[0])}
                            accept="image/*,video/*"
                        />
                    </div>

                    <div className="modal-actions">
                        <Button variant="secondary" onClick={onClose}>Cancel</Button>
                        <Button type="submit" disabled={loading}>
                            {loading ? 'Sharing...' : 'Share Now'}
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateStoryModal;
