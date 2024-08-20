import React, { useState, useEffect } from 'react';
import Swal from 'sweetalert2';
import '../style.css';
import axios from '../AxiosConfig';

const AddNote = ({ noteId, onNoteUpdated, onClose }) => {
  const [note, setNote] = useState({
    title: '',
    content: '',
    archived: false,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNote((prevNote) => ({
      ...prevNote,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!note.title || !note.content) {
      Swal.fire({
        title: 'Empty Fields',
        text: 'Please fill in all fields before submitting the form.',
        icon: 'warning',
        confirmButtonText: 'OK',
      });
      return;
    }

    try {
      if (noteId) {
        await axios.put(`http://localhost:8080/notes/${noteId}`, note);
      } else {
        await axios.post('http://localhost:8080/notes/', note);
      }

      Swal.fire({
        icon: 'success',
        title: 'Success!',
        text: 'The note has been saved successfully.',
      });

      setNote({
        title: '',
        content: '',
        archived: false,
      });

      if (onNoteUpdated) {
        onNoteUpdated(); 
      }

      if (onClose) {
        onClose();
      }

    } catch (error) {
      Swal.fire({
        title: 'Error',
        text: error,
        icon: 'warning',
        confirmButtonText: 'OK',
    });
    }
  };

  useEffect(() => {
    const loadNoteForEditing = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/notes/${noteId}`);
        setNote(response.data);
      } catch (error) {
        Swal.fire({
          title: 'Error',
          text: error,
          icon: 'warning',
          confirmButtonText: 'OK',
      });
      }
    };

    if (noteId) {
      loadNoteForEditing();
    }
  }, [noteId]);

  return (
    <div>
      <h2>{noteId ? 'Edit Note' : 'Add Note'}</h2>
      <form onSubmit={handleSubmit} >
        <div className="mb-3">
          <label className="form-label">
            Title:
            <input className="form-control" type="text" name="title" value={note.title} onChange={handleChange} />
          </label>
        </div>
        <div  className="mb-3">
          <label className="form-label">
            Content:
            <textarea className="form-control" name="content" value={note.content} onChange={handleChange} />
          </label>
        </div>
        <div className="mt-3 mb-3">
          <button className="btn btn-warning" type="submit">{noteId ? 'Save Changes' : 'Save'}</button>
          <button className="btn btn-light" type="button" onClick={onClose}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default AddNote;
