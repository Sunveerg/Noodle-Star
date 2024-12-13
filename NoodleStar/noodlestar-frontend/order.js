window.menuItems = 0;
window.responseFee = 0;

// DOM elements
const street = document.getElementById("street");
const city = document.getElementById("city");
const zipcode = document.getElementById("zipcode");
const fname = document.getElementById("name");
const lastname = document.getElementById("lastname");
const phonenumber = document.getElementById("phonenumber");

const clothingTotal = document.getElementById("price");
const orderTotal = document.getElementById("total");
const menuItemsContainer = document.getElementById('menu-items-container'); // Ensure this exists in your HTML

async function fetchMenuItems() {
  try {
    const response = await fetch('http://localhost:8080/api/v1/menu', {
      method: 'GET',
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch menu items: ${response.status}`);
    }

    const menuItems = await response.json();
    
    // Clear any existing menu items in the container
    menuItemsContainer.innerHTML = "";

    // Dynamically create and add menu items to the container
    menuItems.forEach(item => {
      const menuItemHTML = `
        <div class="clothing-option">
          <input type="checkbox" class="clothing" value="${item.price}" />
          <label>${item.name} - $${(item.price).toFixed(2)}</label>
        </div>
      `;
      menuItemsContainer.innerHTML += menuItemHTML;
    });

    // Bind event listeners to newly created checkboxes
    bindMenuItemEventListeners();

  } catch (error) {
    console.error('Error fetching menu items:', error);
  }
}

function bindMenuItemEventListeners() {
  const foodItems = document.getElementsByClassName("clothing");

  for (const clothing of foodItems) {
    clothing.addEventListener("click", callfeeAPI);
  }
}

async function getFee() {
  try {
    const response = await fetch('http://localhost:8080/api/v1/fee', {
      method: 'GET',
    });
    if (!response.ok) {
      throw new Error('Failed to fetch fee');
    }
    return response.json();
  } catch (error) {
    console.error('Error fetching fee:', error);
    return null;
  }
}

async function callfeeAPI({ target }) {
    console.log(`Item: ${target.value}, Checked: ${target.checked}`);

    // Adjust total based on item selection
    if (target.className === "clothing" && target.checked) {
        window.menuItems += parseInt(target.value, 10);
    } else if (target.className === "clothing" && !target.checked) {
        window.menuItems -= parseInt(target.value, 10);
    }

    // Fetch fee and update totals
    let response = await getFee();
    if (response) {
        window.responseFee = response.data.fee;
        orderTotal.textContent = `$${((Number(window.menuItems) + response.data.fee / 100)).toFixed(2)}`;
        console.log(window.menuItems);
    } else {
        orderTotal.textContent = `$${(Number(window.menuItems)).toFixed(2)}`;
    }

    // Update the price text
    clothingTotal.textContent = `$${(window.menuItems).toFixed(2)}`;
}

// Event listeners for form inputs
street.addEventListener("focusout", callfeeAPI);
city.addEventListener("focusout", callfeeAPI);
zipcode.addEventListener("focusout", callfeeAPI);
fname.addEventListener("focusout", callfeeAPI);
lastname.addEventListener("focusout", callfeeAPI);
phonenumber.addEventListener("focusout", callfeeAPI);

// Fetch menu items when the page loads
window.addEventListener("load", fetchMenuItems);
