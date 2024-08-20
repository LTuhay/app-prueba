// src/Components/Login.js
import React, { useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import '../style.css';

const Login = ({ onLogin }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/auth/login', { username, password });
            const { token, username: loggedInUser } = response.data;
            localStorage.setItem('token', token);
            localStorage.setItem('username', loggedInUser);
            Swal.fire({
                title: 'Success',
                text: 'Logged in successfully',
                icon: 'success',
                confirmButtonText: 'OK',
            });
            onLogin(); 
        } catch (error) {
            Swal.fire({
                title: 'Error',
                text: 'Invalid username or password',
                icon: 'error',
                confirmButtonText: 'OK',
            });
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleLogin} className="login-form">
                <h2>Login</h2>
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-warning">Login</button>
            </form>
        </div>
    );
};

export default Login;
