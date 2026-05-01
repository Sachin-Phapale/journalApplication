import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { journalService } from '../services/journalService';

const CreateEntry = () => {
  const [formData, setFormData] = useState({
    title: '',
    content: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!formData.title.trim()) {
      setError('Title is required');
      return;
    }

    if (!formData.content.trim()) {
      setError('Content is required');
      return;
    }

    setLoading(true);

    try {
      await journalService.createEntry(formData);
      navigate('/entries', { 
        state: { message: 'Journal entry created successfully!' }
      });
    } catch (error) {
      setError(error.response?.data || 'Failed to create entry');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto py-6 sm:px-6 lg:px-8">
      <div className="px-4 py-6 sm:px-0">
        <div className="mb-6">
          <button
            onClick={() => navigate('/entries')}
            className="flex items-center text-gray-600 hover:text-gray-900 mb-4"
          >
            <span className="h-5 w-5 mr-2">⬅️</span>
            Back to Entries
          </button>
          
          <div className="flex items-center">
            <span className="text-2xl text-blue-600">📖</span>
            <h1 className="text-3xl font-bold text-gray-900">Create New Entry</h1>
          </div>
        </div>

        <div className="card">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
                Title
              </label>
              <input
                id="title"
                name="title"
                type="text"
                required
                className="input-field text-lg"
                placeholder="Give your entry a title..."
                value={formData.title}
                onChange={handleChange}
              />
            </div>

            <div>
              <label htmlFor="content" className="block text-sm font-medium text-gray-700 mb-2">
                Content
              </label>
              <textarea
                id="content"
                name="content"
                required
                rows={12}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                placeholder="Write your thoughts here..."
                value={formData.content}
                onChange={handleChange}
              />
              <div className="mt-2 text-sm text-gray-500">
                {formData.content.length} characters
              </div>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded">
                {error}
              </div>
            )}

            <div className="flex justify-end space-x-4">
              <button
                type="button"
                onClick={() => navigate('/entries')}
                className="btn-secondary"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={loading}
                className="btn-primary disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
              >
              <span className="h-5 w-5 mr-2">💾</span>
                {loading ? 'Saving...' : 'Save Entry'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateEntry;
