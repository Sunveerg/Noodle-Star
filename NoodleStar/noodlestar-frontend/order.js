window.menuItems = 0;

const street = document.getElementById("street");
const city = document.getElementById("city");
const zipcode = document.getElementById("zipcode");
const fname = document.getElementById("name");
const lastname = document.getElementById("lastname");
const phonenumber = document.getElementById("phonenumber");

const foodItems = document.getElementsByClassName("clothing");
const clothingTotal = document.getElementById("price");
const orderTotal= document.getElementById("total");

async function callfeeAPI({ target }) {
    console.log(`Item: ${target.value}, Checked: ${target.checked}`);
    if (target.className === "clothing" && target.checked) {
        window.menuItems += parseInt(target.value, 10);
    } else if (target.className === "clothing" && !target.checked) {
        window.menuItems -= parseInt(target.value, 10);
    }

    let response = await getFee()
    if (response){
        window.responseFee = response.data.fee

        orderTotal.textContent = `$${((Number(window.menuItems) + response.data.fee) / 100).toFixed(2)}`;
        console.log(window.menuItems)
    }else{
        orderTotal.textContent = `$${(Number(window.menuItems) / 100).toFixed(2)}`
    }



    // Update the price text
    clothingTotal.textContent = `$${(window.menuItems / 100).toFixed(2)}`;
}

for (const clothing of foodItems) {
    clothing.addEventListener("click", callfeeAPI); 
}

street.addEventListener("focusout", callfeeAPI)
city.addEventListener("focusout", callfeeAPI)
zipcode.addEventListener("focusout", callfeeAPI)
fname.addEventListener("focusout", callfeeAPI)
lastname.addEventListener("focusout", callfeeAPI)
phonenumber.addEventListener("focusout", callfeeAPI)
