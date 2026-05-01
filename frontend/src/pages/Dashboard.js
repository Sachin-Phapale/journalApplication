import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { journalService } from '../services/journalService';

const Dashboard = () => {
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalEntries: 0,
    thisWeek: 0,
    thisMonth: 0
  });
  
  const { user } = useAuth();

  useEffect(() => {
    fetchEntries();
  }, []);

  const fetchEntries = async () => {
    try {
      const response = await journalService.getAllEntries();
      setEntries(response.data || []);
      calculateStats(response.data || []);
    } catch (error) {
      console.error('Failed to fetch entries:', error);
    } finally {
      setLoading(false);
    }
  };

  const calculateStats = (entries) => {
    const now = new Date();
    const oneWeekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
    const oneMonthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);

    const thisWeek = entries.filter(entry => 
      new Date(entry.date) > oneWeekAgo
    ).length;

    const thisMonth = entries.filter(entry => 
      new Date(entry.date) > oneMonthAgo
    ).length;

    setStats({
      totalEntries: entries.length,
      thisWeek,
      thisMonth
    });
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
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">
            Welcome back, {user?.userName}!
          </h1>
          <p className="mt-2 text-gray-600">
            Here's what's happening with your journal today.
          </p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="card">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl text-blue-600">📖</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-500">Total Entries</p>
                <p className="text-2xl font-bold text-gray-900">{stats.totalEntries}</p>
              </div>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl text-green-600">📅</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-500">This Week</p>
                <p className="text-2xl font-bold text-gray-900">{stats.thisWeek}</p>
              </div>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <span className="text-2xl text-purple-600">📈</span>
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-500">This Month</p>
                <p className="text-2xl font-bold text-gray-900">{stats.thisMonth}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="mb-8">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Quick Actions</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Link
              to="/create-entry"
              className="card hover:shadow-lg transition-shadow duration-200 group"
            >
              <div className="flex items-center">
                <span className="text-2xl text-blue-600">➕</span>
                <div className="ml-4">
                  <h3 className="text-lg font-medium text-gray-900">Create New Entry</h3>
                  <p className="text-gray-600">Write about your day</p>
                </div>
              </div>
            </Link>

            <Link
              to="/entries"
              className="card hover:shadow-lg transition-shadow duration-200 group"
            >
              <div className="flex items-center">
                <span className="text-2xl text-green-600">📖</span>
                <div className="ml-4">
                  <h3 className="text-lg font-medium text-gray-900">View All Entries</h3>
                  <p className="text-gray-600">Read your past thoughts</p>
                </div>
              </div>
            </Link>
          </div>
        </div>

        {/* Recent Entries */}
        <div>
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold text-gray-900">Recent Entries</h2>
            <Link
              to="/entries"
              className="text-blue-600 hover:text-blue-500 text-sm font-medium"
            >
              View all
            </Link>
          </div>
          
          {entries.length === 0 ? (
            <div className="card text-center py-12">
              <span className="text-5xl text-gray-400">📖</span>
              <h3 className="text-lg font-medium text-gray-900 mb-2">No entries yet</h3>
              <p className="text-gray-600 mb-4">Start your journaling journey today!</p>
              <Link
                to="/create-entry"
                className="btn-primary"
              >
                Create Your First Entry
              </Link>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {entries.slice(0, 6).map((entry) => (
                <div key={entry.id} className="card hover:shadow-lg transition-shadow duration-200">
                  <h3 className="text-lg font-medium text-gray-900 mb-2 line-clamp-2">
                    {entry.title}
                  </h3>
                  <p className="text-gray-600 text-sm mb-3 line-clamp-3">
                    {entry.content}
                  </p>
                  <div className="flex justify-between items-center text-sm text-gray-500">
                    <span>{new Date(entry.date).toLocaleDateString()}</span>
                    <Link
                      to={`/edit-entry/${entry.id}`}
                      className="text-blue-600 hover:text-blue-500"
                    >
                      Edit
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
