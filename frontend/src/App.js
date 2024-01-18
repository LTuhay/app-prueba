import React, { useState, useEffect } from 'react';
import AddNote from './Components/AddNote';
import NoteListConditional from './Components/NoteListConditional';
import NewCategoryForm from './Components/NewCategoryForm';

const App = () => {
  const [activeComponent, setActiveComponent] = useState('allNotes');
  const [isAddingCategory, setIsAddingCategory] = useState(false);

  const showAddNote = () => {
    setActiveComponent('addNote');
  };

  const showAddCategory = () => {
    setIsAddingCategory(true);
  };


  const showActiveNotes = () => {
    setActiveComponent('activeNotes');
  };

  const showArchivedNotes = () => {
    setActiveComponent('archivedNotes');
  };

  useEffect(() => {
    showActiveNotes();
  }, []); 

  return (
    <div>


      <div className='container d-flex flex-column align-items-center justify-content-center p-4'>
        <div>
          <nav className="mt-3 mb-3">
            <button className="btn btn-light" onClick={showActiveNotes}>Active Notes</button>
            <button className="btn btn-light" onClick={showArchivedNotes}>Archived Notes</button>
          </nav>
        </div>
        <div className="mt-3 mb-3">
          {activeComponent === 'addNote' && <AddNote />}
          {activeComponent === 'activeNotes' && <NoteListConditional showArchived={false} />}
          {activeComponent === 'archivedNotes' && <NoteListConditional showArchived={true} />}
        </div>
        <div className="mt-3 mb-3">
          <button className="btn btn-light" onClick={showAddNote}>Add Note</button>
          <button className="btn btn-light" onClick={showAddCategory}>Add Category</button>
        </div>
        {isAddingCategory && <NewCategoryForm onCategoryAdded={() => setIsAddingCategory(false)} />}
      </div>
    </div>
  );
};

export default App;
