// src/App.js
import React, { useState, useEffect } from 'react';
import NoteListConditional from './Components/NoteListConditional';
import Login from './Components/Login';
import Swal from 'sweetalert2';

const App = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('token'));
    const [activeComponent, setActiveComponent] = useState('activeNotes');
    const username = localStorage.getItem('username');

    const showActiveNotes = () => {
        setActiveComponent('activeNotes');
    };

    const showArchivedNotes = () => {
        setActiveComponent('archivedNotes');
    };

    const handleLogin = () => {
        setIsAuthenticated(true);
    };

    const handleLogout = () => {
      Swal.fire({
        title: 'Goodbye!',
        text: 'You have been logged out.',
        icon: 'info',
        confirmButtonText: 'OK',
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            setIsAuthenticated(false);
        }
    });
    };

    useEffect(() => {
        if (isAuthenticated) {
            showActiveNotes();
        }
    }, [isAuthenticated]);

    return (
        <div>
            {!isAuthenticated ? (
                <Login onLogin={handleLogin} />
            ) : (
                <div className='container d-flex flex-column align-items-center justify-content-center p-4'>
                    <div>
                        <nav className="mt-1 mb-1">
                            {username && (
                                <div className="welcome-message" >
                                    <span>Welcome, {username}!</span>
                                </div>
                            )}
                            <button className="btn btn-light" onClick={showActiveNotes}>Active Notes</button>
                            <button className="btn btn-light" onClick={showArchivedNotes}>Archived Notes</button>
                            <button className="btn btn-light" onClick={handleLogout}>Logout</button>
                        </nav>
                    </div>
                    <div className="mt-1 mb-1">
                        {activeComponent === 'activeNotes' && <NoteListConditional showArchived={false} />}
                        {activeComponent === 'archivedNotes' && <NoteListConditional showArchived={true} />}
                    </div>
                </div>
            )}
        </div>
    );
};

export default App;
