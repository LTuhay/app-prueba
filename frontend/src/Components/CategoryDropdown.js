import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import '../style.css';

const CategoryDropdown = ({ onCategorySelect }) => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get('http://localhost:8080/categories/all');
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

    fetchCategories();
  }, []);

  const handleCategorySelect = (category) => {
    onCategorySelect(category);
  };

  return (
    <div>
      <select onChange={(e) => handleCategorySelect(e.target.value)}>
        <option value="">Select category</option>
        {categories.map((category) => (
          <option key={category.id} value={category.name}>
            {category.name}
          </option>
        ))}
      </select>
    </div>
  );
};

export default CategoryDropdown;
