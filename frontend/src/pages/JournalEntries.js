import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import { journalService } from '../services/journalService';

const JournalEntries = () => {
  const [entries, setEntries] = useState([]);
  const [filteredEntries, setFilteredEntries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [error, setError] = useState('');
  
  const location = useLocation();

  useEffect(() => {
    fetchEntries();
  }, []);

  useEffect(() => {
    if (searchTerm) {
      const filtered = entries.filter(entry =>
        entry.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        entry.content.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredEntries(filtered);
    } else {
      setFilteredEntries(entries);
    }
  }, [searchTerm, entries]);

  const fetchEntries = async () => {
    try {
      const response = await journalService.getAllEntries();
      setEntries(response.data || []);
      setFilteredEntries(response.data || []);
    } catch (error) {
      setError('Failed to fetch entries');
      console.error('Failed to fetch entries:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this entry?')) {
      try {
        await journalService.deleteEntry(id);
        setEntries(entries.filter(entry => entry.id !== id));
        setFilteredEntries(filteredEntries.filter(entry => entry.id !== id));
      } catch (error) {
        setError('Failed to delete entry');
        console.error('Failed to delete entry:', error);
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div className="px-4 py-6 sm:px-0">
        <div className="mb-6">
          <div className="flex justify-between items-center mb-4">
            <h1 className="text-3xl font-bold text-gray-900">My Journal Entries</h1>
            <Link
              to="/create-entry"
              className="btn-primary flex items-center"
            >
              <span className="h-5 w-5 mr-2">➕</span>
              New Entry
            </Link>
          </div>

          {location.state?.message && (
            <div className="bg-green-50 border border-green-200 text-green-600 px-4 py-3 rounded mb-4">
              {location.state.message}
            </div>
          )}

          {/* Search Bar */}
          <div className="relative mb-6">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <span className="text-5 w-5 mr-2">🔍</span>
            </div>
            <input
              type="text"
              className="input-field pl-10"
              placeholder="Search entries..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        {filteredEntries.length === 0 ? (
          <div className="card text-center py-12">
            <span className="text-5xl text-gray-400">📖</span>
            <h3 className="text-lg font-medium text-gray-900 mb-2">
              {entries.length === 0 ? 'No entries yet' : 'No matching entries'}
            </h3>
            <p className="text-gray-600 mb-4">
              {entries.length === 0 
                ? 'Start your journaling journey today!' 
                : 'Try adjusting your search terms'
              }
            </p>
            {entries.length === 0 && (
              <Link
                to="/create-entry"
                className="btn-primary"
              >
                Create Your First Entry
              </Link>
            )}
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredEntries.map((entry) => (
              <div key={entry.id} className="card hover:shadow-lg transition-shadow duration-200">
                <div className="mb-4">
                  <h3 className="text-lg font-medium text-gray-900 mb-2 line-clamp-2">
                    {entry.title}
                  </h3>
                  <div className="flex items-center text-sm text-gray-500 mb-3">
                    <Calendar className="h-4 w-4 mr-1" />
                    {new Date(entry.date).toLocaleDateString('en-US', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric'
                    })}
                  </div>
                  <p className="text-gray-600 line-clamp-4">
                    {entry.content}
                  </p>
                </div>
                
                <div className="flex justify-between items-center pt-4 border-t border-gray-200">
                  <div className="flex space-x-2">
                    <Link
                      to={`/edit-entry/${entry.id}`}
                      className="text-blue-600 hover:text-blue-500 p-2 hover:bg-blue-50 rounded"
                    >
                  <span className="text-4 w-4">✏️</span>
                    </Link>
                    <button
                      onClick={() => handleDelete(entry.id)}
                      className="text-red-600 hover:text-red-500 p-2 hover:bg-red-50 rounded"
                    >
                <span className="text-4 w-4">🗑️</span>
                    </button>
                  </div>
                  <div className="text-sm text-gray-500">
                    {entry.content.length} characters
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default JournalEntries;
