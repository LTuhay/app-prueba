import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import '../style.css';

const ActiveNotes = () => {
  const [activeNotes, setActiveNotes] = useState([]);

  useEffect(() => {
    const fetchActiveNotes = async () => {
      try {
        const response = await axios.get('/api/notes?archived=false');
        setActiveNotes(response.data);
      } catch (error) {
        Swal.fire({
          title: 'Error',
          text: error,
          icon: 'warning',
          confirmButtonText: 'OK',
        });
      }
    };

    fetchActiveNotes();
  }, []);

  return (
    <div>
      <h2>Active Notes</h2>
      <ul>
        {activeNotes.map((note) => (
          <li key={note.id}>{note.title}</li>
        ))}
      </ul>
    </div>
  );
};

export default ActiveNotes;
