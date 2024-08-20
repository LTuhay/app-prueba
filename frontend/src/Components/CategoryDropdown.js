import React from 'react';

const CategoryDropdown = ({ categories, onCategorySelect }) => {
  const handleCategorySelect = (event) => {
    onCategorySelect(event.target.value);
  };

  return (
    <div>
      <select onChange={handleCategorySelect}>
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
