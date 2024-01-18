import React, { useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import '../style.css';

const NewCategoryForm = ({ onCategoryAdded }) => {
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
      const response = await axios.post('http://localhost:8080/categories/add', {
        name: newCategoryName,
      });

      if (response.status === 201) {
        onCategoryAdded(newCategoryName);
        setNewCategoryName(''); 
        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: 'The new category has been saved successfully.',
      });
      } else {
        Swal.fire({
          title: 'Error',
          text: 'The new category could not be saved',
          icon: 'warning',
          confirmButtonText: 'OK',
        });
      }
    } catch (error) {
      console.error('Error de red:', error);
    }
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Nueva categoría"
        value={newCategoryName}
        onChange={(e) => setNewCategoryName(e.target.value)}
      />
      <button className="btn btn-warning" onClick={handleAddCategory}>Add new category</button>
    </div>
  );
};

export default NewCategoryForm;
