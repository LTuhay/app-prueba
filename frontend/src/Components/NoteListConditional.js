import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import AddNote from '../Components/AddNote';
import CategoryDropdown from '../Components/CategoryDropdown';
import NewCategoryForm from './NewCategoryForm';
import Swal from 'sweetalert2';
import '../style.css';
import axios from '../AxiosConfig';

Modal.setAppElement('#root');

const NoteListConditional = ({ showArchived }) => {
    const [notes, setNotes] = useState([]);
    const [categories, setCategories] = useState([]); 
    const [editingNote, setEditingNote] = useState(null);
    const [viewingNote, setViewingNote] = useState(null);
    const [categoryStates, setCategoryStates] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [filterResetKey, setFilterResetKey] = useState(0);
    const [addCategoryResetKeys, setAddCategoryResetKeys] = useState([]);
    const [showAddNoteModal, setShowAddNoteModal] = useState(false);
    const [showAddCategoryModal, setShowAddCategoryModal] = useState(false);

    useEffect(() => {
        fetchNotes(showArchived, selectedCategory);
        fetchCategories(); 
    }, [showArchived, selectedCategory]);

    const fetchNotes = async (showArchived, selectedCategory = '') => {
        try {
            const response = await axios.get(`http://localhost:8080/notes/${showArchived ? 'archived' : 'not-archived'}`);
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

    const fetchCategories = async () => {
        try {
            const response = await axios.get('http://localhost:8080/categories/');
            setCategories(response.data); 
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
        handleCloseView();  
        setShowAddNoteModal(true);  
    };

    const handleDelete = async (noteId) => {
        const result = await Swal.fire({
            title: 'Are you sure?',
            text: "Do you really want to delete this note? This action cannot be undone.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'Cancel'
        });
    
        if (result.isConfirmed) {
            try {
                await axios.delete(`http://localhost:8080/notes/${noteId}`);
                fetchNotes(showArchived);
                handleCloseView();
                Swal.fire('Deleted!', 'The note has been deleted.', 'success');
            } catch (error) {
                Swal.fire({
                    title: 'Error',
                    text: error,
                    icon: 'warning',
                    confirmButtonText: 'OK',
                });
            }
        }
    };

    const handleArchive = async (noteId, archived) => {
        try {
            await axios.patch(`http://localhost:8080/notes/${noteId}/archive-status`, null, {
                params: { archived: archived }
            });
            fetchNotes(showArchived);
            handleCloseView();
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
            const response = await axios.post(`http://localhost:8080/notes/${notes[index].id}/category`, null, {
                params: {
                    categoryName: newCategoryName
                }
            });

            if (response.status === 200) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Category has been added successfully.',
                });

                const newResetKeys = [...addCategoryResetKeys];
                newResetKeys[index] = (newResetKeys[index] || 0) + 1;
                setAddCategoryResetKeys(newResetKeys);
            } else {
                Swal.fire({
                    title: 'Error',
                    text: 'Error',
                    icon: 'warning',
                    confirmButtonText: 'OK',
                });
            }
            fetchNotes(showArchived, selectedCategory);
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
        const result = await Swal.fire({
            title: 'Are you sure?',
            text: `Do you really want to remove the category "${categoryName}" from this note?`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, remove it!',
            cancelButtonText: 'Cancel'
        });
    
        if (result.isConfirmed) {
            try {
                const response = await axios.delete(`http://localhost:8080/notes/${noteId}/category`, {
                    params: {
                        categoryName: categoryName
                    }
                });
    
                if (response.status === 200) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Removed!',
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
        }
    };

    const handleFilterReset = () => {
        setSelectedCategory(null);
        setFilterResetKey((prevKey) => prevKey + 1);
    };

    const handleViewNote = (note) => {
        setViewingNote(note);
    };

    const handleCloseView = () => {
        setViewingNote(null);
    };

    const handleAddCategoryClose = () => {
        setShowAddCategoryModal(false);
        fetchCategories(); 
    };

    const handleAddNoteClose = () => {
        setShowAddNoteModal(false);
        setEditingNote(null);
    };
    

    return (
        <div>
            <button className="btn btn-warning mt-1" onClick={() => setShowAddNoteModal(true)}>Create Note</button>
            <button className="btn btn-warning mt-1" onClick={() => setShowAddCategoryModal(true)}>Create Category</button>             
            <div className="mt-3">
                <h5>Filter categories</h5>
                <CategoryDropdown 
                    categories={categories} 
                    key={filterResetKey} 
                    onCategorySelect={setSelectedCategory} 
                />
                <button className="btn btn-light mt-3" onClick={handleFilterReset}>
                    Clean filter
                </button>
            </div>
         
            <h2>{showArchived ? 'Archived Notes' : 'Notes'}</h2>   
            {notes.length === 0 ? (
                <p className='no-notes'>No notes to display.</p>
            ) : (
                <table className="table table-hover">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Categories</th>
                            <th>Add category</th>
                        </tr>
                    </thead>
                    <tbody>
                        {notes.map((note, index) => (
                            <tr key={note.id}>
                                <td>
                                    <span
                                        style={{ cursor: 'pointer', textDecoration: 'underline' }}
                                        onClick={() => handleViewNote(note)}
                                    >
                                        {note.title}
                                    </span>
                                </td>
                                <td>
                                    <div>
                                        {note.categoryNames && note.categoryNames.length > 0 ? (
                                            <ul>
                                                {note.categoryNames.map((categoryName) => (
                                                    <li
                                                        className='category-name'
                                                        key={categoryName}
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
                                    <CategoryDropdown 
                                        categories={categories} 
                                        key={addCategoryResetKeys[index] || 0}
                                        onCategorySelect={(categoryName) => handleCategorySelect(categoryName, index)} 
                                    />
                                    <button className="btn btn-warning" onClick={() => handleAddSelectedCategory(index)}>
                                        Add selected category
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
    
            {/* AddNote modal */}
            <Modal
                isOpen={showAddNoteModal}
                onRequestClose={handleAddNoteClose} 
                contentLabel="Add Note Modal"
                className="Modal"
                overlayClassName="Overlay"
            >
                <AddNote 
                    noteId={editingNote ? editingNote.id : null} 
                    onNoteUpdated={() => fetchNotes(showArchived)} 
                    onClose={handleAddNoteClose} 
                />
            </Modal>

    
            {/* ViewNote modal */}
            <Modal
                isOpen={!!viewingNote}
                onRequestClose={handleCloseView}
                contentLabel="View Note Modal"
                className="Modal"
                overlayClassName="Overlay"
            >
                {viewingNote && (
                    <div>
                        <h2>{viewingNote.title}</h2>
                        <div>
                            <p className='note-content'>{viewingNote.content}</p>
                        </div>
                        <button className="btn btn-warning" onClick={() => handleEdit(viewingNote)}>Edit</button>
                        <button 
                            className="btn btn-light" 
                            onClick={() => handleArchive(viewingNote.id, !viewingNote.archived)}
                        >
                            {viewingNote.archived ? 'Unarchive' : 'Archive'}
                        </button>
                        <button className="btn btn-light" onClick={() => handleDelete(viewingNote.id)}>Delete</button>
                        <button className="btn btn-light" onClick={handleCloseView}>Close</button>
                    </div>
                )}
            </Modal>
    
            {/* AddCategory modal */}
            <Modal
                isOpen={showAddCategoryModal}
                onRequestClose={handleAddCategoryClose}
                contentLabel="Add Category Modal"
                className="Modal"
                overlayClassName="Overlay"
            >
                <NewCategoryForm 
                    onClose={handleAddCategoryClose} 
                />
            </Modal>           
        </div>
    );
};

export default NoteListConditional;
