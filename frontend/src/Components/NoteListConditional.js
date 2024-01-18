import React, { useState, useEffect } from 'react';
import AddNote from '../Components/AddNote';
import CategoryDropdown from '../Components/CategoryDropdown';
import axios from 'axios';
import Swal from 'sweetalert2';
import '../style.css';

const NoteListConditional = ({ showArchived }) => {
    const [notes, setNotes] = useState([]);
    const [editingNote, setEditingNote] = useState(null);
    const [categoryStates, setCategoryStates] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);


    useEffect(() => {
        fetchNotes(showArchived, selectedCategory);
    }, [showArchived, selectedCategory]);

    const fetchNotes = async (showArchived, selectedCategory = '') => {
        try {
            const response = await axios.get(`http://localhost:8080/notes/${showArchived ? 'archived' : 'notArchived'}`);
            let filteredNotes = response.data;

            if (selectedCategory) {
                filteredNotes = filteredNotes.filter((note) => note.categoryNames.includes(selectedCategory));
            }

            setNotes(filteredNotes);
            setCategoryStates(filteredNotes.map(() => ({ selectedCategoryName: '' })));
        } catch (error) {
            Swal.fire({
                title: 'Error',
                text: error,
                icon: 'warning',
                confirmButtonText: 'OK',
              });
        }
    };

    const handleEdit = (note) => {
        setEditingNote(note);
    };

    const handleCloseEdit = () => {
        setEditingNote(null);
    };

    const handleDelete = async (noteId) => {
        try {
            await axios.delete(`http://localhost:8080/notes/${noteId}`);
            fetchNotes(showArchived);
        } catch (error) {
            Swal.fire({
                title: 'Error',
                text: error,
                icon: 'warning',
                confirmButtonText: 'OK',
              });
        }
    };


    const handleCategorySelect = (selectedCategoryName, index) => {
        const updatedCategoryStates = [...categoryStates];
        updatedCategoryStates[index] = { ...updatedCategoryStates[index], newCategoryName: selectedCategoryName }; 
        setCategoryStates(updatedCategoryStates);
    };

    const handleAddSelectedCategory = async (index) => {
        const newCategoryName = categoryStates[index].newCategoryName; 

        try {
            const response = await axios.post('http://localhost:8080/notes/add-category', {
                noteId: notes[index].id,
                categoryName: newCategoryName, 
            });

            if (response.status === 200) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Category has been added successfully.',
                });
            } else {
                Swal.fire({
                    title: 'Error',
                    text: 'Error',
                    icon: 'warning',
                    confirmButtonText: 'OK',
                  });
            }
            fetchNotes(showArchived,selectedCategory);
        } catch (error) {
            Swal.fire({
                title: 'Error',
                text: error,
                icon: 'warning',
                confirmButtonText: 'OK',
              });
        }
    };

    const handleRemoveCategory = async (noteId, categoryName) => {
        try {
            console.log("id", noteId.toString())
            console.log("categoria", categoryName)
            const response = await axios.delete('http://localhost:8080/notes/remove-category', {
                data: {
                    noteId: noteId.toString(), 
                    categoryName: categoryName,
                },
            });

            if (response.status === 200) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Category removed successfully.',
                });
                const updatedNotes = [...notes];
                const noteIndex = updatedNotes.findIndex((note) => note.id === noteId);
                if (noteIndex !== -1) {
                    updatedNotes[noteIndex].categoryNames = updatedNotes[noteIndex].categoryNames.filter((name) => name !== categoryName);
                    setNotes(updatedNotes);
                }
            } else {
                Swal.fire({
                    title: 'Error',
                    text: 'Error',
                    icon: 'warning',
                    confirmButtonText: 'OK',
                  });
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


    return (
        <div>
            <h2>{showArchived ? 'Archived Notes' : 'Notes'}</h2>

            <table className="table table-hover">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Content</th>
                        <th>Categories</th>
                        <th>Actions</th>
                        <th>Add category</th>
                    </tr>
                </thead>
                <tbody>
                    {notes.map((note, index) => (
                        <tr key={note.id}>
                            <td>{note.title}</td>
                            <td>{note.content}</td>
                            <td>
                                <div>
                                    {note.categoryNames && note.categoryNames.length > 0 ? (
                                        <ul>
                                            {note.categoryNames.map((categoryName) => (
                                                <li
                                                    key={categoryName}
                                                    style={{ cursor: 'pointer', textDecoration: 'underline' }}
                                                    onClick={() => handleRemoveCategory(note.id, categoryName)}
                                                >
                                                    {categoryName}
                                                </li>
                                            ))}
                                        </ul>
                                    ) : (
                                        'No category'
                                    )}
                                </div>
                            </td>
                            <td>
                                <button className="btn btn-light" onClick={() => handleEdit(note)}>
                                    Edit
                                </button>
                                <button className="btn btn-light" onClick={() => handleDelete(note.id)}>
                                    Delete
                                </button>
                            </td>
                            <td>
                                <CategoryDropdown onCategorySelect={(categoryName) => handleCategorySelect(categoryName, index)} />
                                <button className="btn btn-light" onClick={() => handleAddSelectedCategory(index)}>
                                    Add selected category
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {editingNote && <AddNote noteId={editingNote.id} onClose={handleCloseEdit} onNoteUpdated={fetchNotes} />}
            <div>
                <CategoryDropdown onCategorySelect={setSelectedCategory} />
                <button className="btn btn-warning" onClick={() => setSelectedCategory(null)}>
                    Clean filter
                </button>
            </div>

        </div>
    );
};

export default NoteListConditional;
