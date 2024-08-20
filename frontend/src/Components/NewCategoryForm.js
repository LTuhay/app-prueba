import React, { useState } from 'react';
import Swal from 'sweetalert2';
import '../style.css';
import axios from '../AxiosConfig';

const NewCategoryForm = ({ onClose }) => {
  const [newCategoryName, setNewCategoryName] = useState('');

  const handleAddCategory = async () => {
    if (!newCategoryName) {
      Swal.fire({
        title: 'Empty Field',
        text: 'Please fill in all fields before submitting the form.',
        icon: 'warning',
        confirmButtonText: 'OK',
      });
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/categories/', {
        name: newCategoryName,
      });

      if (response.status === 201) {
        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: 'The new category has been saved successfully.',
        });
        setNewCategoryName(''); 
        onClose(); 
      } else {
        Swal.fire({
          title: 'Error',
          text: 'The new category could not be saved',
          icon: 'warning',
          confirmButtonText: 'OK',
        });
      }
    } catch (error) {
      console.error('Network error:', error);
    }
  };

  return (
    <div>
      <h2>Add New Category</h2>
      <input
        type="text"
        placeholder="New category name"
        value={newCategoryName}
        onChange={(e) => setNewCategoryName(e.target.value)}
      />
      <div>
        <button className="btn btn-warning" onClick={handleAddCategory}>Add new category</button>
        <button className="btn btn-light mt-2" onClick={onClose}>Cancel</button>
      </div>

    </div>
  );
};

export default NewCategoryForm;
