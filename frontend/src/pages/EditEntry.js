import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { journalService } from '../services/journalService';

const EditEntry = () => {
  const [formData, setFormData] = useState({
    title: '',
    content: ''
  });
  const [loading, setLoading] = useState(false);
  const [fetchLoading, setFetchLoading] = useState(true);
  const [error, setError] = useState('');
  const [originalEntry, setOriginalEntry] = useState(null);
  
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    fetchEntry();
  }, [id]);

  const fetchEntry = async () => {
    try {
      const response = await journalService.getEntryById(id);
      const entry = response.data;
      setOriginalEntry(entry);
      setFormData({
        title: entry.title,
        content: entry.content
      });
    } catch (error) {
      setError('Failed to fetch entry');
      console.error('Failed to fetch entry:', error);
    } finally {
      setFetchLoading(false);
    }
  };

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
      await journalService.updateEntry(id, formData);
      navigate('/entries', { 
        state: { message: 'Journal entry updated successfully!' }
      });
    } catch (error) {
      setError(error.response?.data || 'Failed to update entry');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this entry?')) {
      try {
        await journalService.deleteEntry(id);
        navigate('/entries', { 
          state: { message: 'Journal entry deleted successfully!' }
        });
      } catch (error) {
        setError('Failed to delete entry');
        console.error('Failed to delete entry:', error);
      }
    }
  };

  if (fetchLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (!originalEntry) {
    return (
      <div className="max-w-4xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="card text-center py-12">
          <h3 className="text-lg font-medium text-gray-900 mb-2">Entry not found</h3>
          <p className="text-gray-600 mb-4">The entry you're looking for doesn't exist.</p>
          <button
            onClick={() => navigate('/entries')}
            className="btn-primary"
          >
            Back to Entries
          </button>
        </div>
      </div>
    );
  }

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
          
          <div className="flex justify-between items-center">
            <div className="flex items-center">
              <span className="text-2xl text-blue-600">📖</span>
              <h1 className="text-3xl font-bold text-gray-900">Edit Entry</h1>
            </div>
            <button
              onClick={handleDelete}
              className="text-red-600 hover:text-red-500 p-2 hover:bg-red-50 rounded"
            >
              <span className="h-5 w-5">🗑️</span>
            </button>
          </div>
        </div>

        <div className="mb-4 text-sm text-gray-500">
          Originally created: {new Date(originalEntry.date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
          })}
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
                {loading ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditEntry;
