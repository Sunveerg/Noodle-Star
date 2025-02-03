window.menuItems = 0;
window.responseFee = 0;

// DOM elements
const street = document.getElementById('street');
const city = document.getElementById('city');
const zipcode = document.getElementById('zipcode');
const fname = document.getElementById('name');
const lastname = document.getElementById('lastname');
const phonenumber = document.getElementById('phonenumber');

const clothingTotal = document.getElementById('price');
const orderTotal = document.getElementById('total');
const menuItemsContainer = document.getElementById('menu-items-container'); // Ensure this exists in your HTML

function fetchMenuItemsFromCookies() {
  // Retrieve menu items from cookies
  const menuItems = JSON.parse(Cookies.get('cartItems') || '[]');
  console.log('Cart Items from Cookies:', Cookies.get('cartItems'));

  // Clear any existing menu items in the container
  menuItemsContainer.innerHTML = '';

  // Dynamically create and add menu items to the container
  menuItems.forEach(item => {
    // Validate that price exists and is a valid number
    const price = item.price && !isNaN(item.price) ? item.price : 0;

    const menuItemHTML = `
      <div class="clothing-option">
        <input type="checkbox" class="clothing" value="${price}" />
        <label>${item.name || 'Unknown Item'} - $${price.toFixed(2)}</label>
      </div>
    `;
    menuItemsContainer.innerHTML += menuItemHTML;
  });

  // Bind event listeners to newly created checkboxes
  bindMenuItemEventListeners();
}

function bindMenuItemEventListeners() {
  const foodItems = document.getElementsByClassName('clothing');

  for (const clothing of foodItems) {
    clothing.addEventListener('click', callfeeAPI);
  }
}

async function getFee() {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  try {
    const response = await fetch(`${backendUrl}/api/v1/fee`, {
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
  if (target.className === 'clothing' && target.checked) {
    window.menuItems += parseInt(target.value, 10);
  } else if (target.className === 'clothing' && !target.checked) {
    window.menuItems -= parseInt(target.value, 10);
  }

  // Fetch fee and update totals
  let response = await getFee();
  if (response) {
    window.responseFee = response.data.fee;
    orderTotal.textContent = `$${(Number(window.menuItems) + response.data.fee / 100).toFixed(2)}`;
    console.log(window.menuItems);
  } else {
    orderTotal.textContent = `$${Number(window.menuItems).toFixed(2)}`;
  }

  // Update the price text
  clothingTotal.textContent = `$${window.menuItems.toFixed(2)}`;

  // Save updated cart items in cookies
  saveMenuItemsToCookies();
}

// Save menu items to cookies
function saveMenuItemsToCookies() {
  const menuItems = [];
  const foodItems = document.getElementsByClassName('clothing');

  for (const clothing of foodItems) {
    if (clothing.checked) {
      menuItems.push({
        name: clothing.nextElementSibling.textContent.split(' - ')[0], // Extract name
        price: parseFloat(clothing.value),
      });
    }
  }

  // Store the menu items in cookies
  Cookies.set('cartItems', JSON.stringify(menuItems), { expires: 7, path: '' }); // Expires in 7 days
}

// Event listeners for form inputs
street.addEventListener('focusout', callfeeAPI);
city.addEventListener('focusout', callfeeAPI);
zipcode.addEventListener('focusout', callfeeAPI);
fname.addEventListener('focusout', callfeeAPI);
lastname.addEventListener('focusout', callfeeAPI);
phonenumber.addEventListener('focusout', callfeeAPI);

// Fetch menu items from cookies when the page loads
window.addEventListener('load', fetchMenuItemsFromCookies);
