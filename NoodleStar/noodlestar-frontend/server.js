import express from "express";
import path from "path";
import { fileURLToPath } from "url";
import { DoorDashClient } from "@doordash/sdk";
import { v4 as uuidv4 } from "uuid";
import "dotenv/config";

// Create an Express application
const app = express();
const port = 3001;

let externalDeliveryId = null;
// Use express.json() to parse JSON body
app.use(express.urlencoded({extended : false}))
app.use(express.json());


// Get the directory name of the current file
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Serve the React app's HTML file
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "order.html"));
});

app.post('/test-post', (req, res) => {
  res.send('POST request received!');
});


app.get("/test-get", (req, res) => {
  res.send("GET request received!");
});

// Start the Express server
app.listen(port, (err) => {
  if (err) {
    return console.log("Error", err);
  }
  console.log(`App listening on port ${port}`);
});

app.use(express.static(__dirname ))
app.set("view engine", "pug")
app.get("/order2", (req,res)=>{
  res.render("order2")
})

// POST request to get the delivery rate
app.post("/get-delivery-rate", async (req, res) => {
  const client = new DoorDashClient({
    developer_id: process.env.DEVELOPER_ID,
    key_id: process.env.KEY_ID,
    signing_secret: process.env.SIGNING_SECRET,
  });


    const response = await client.deliveryQuote({
      external_delivery_id: uuidv4(),
      pickup_address: "11 Madison Ave, New York, NY 10010",
      pickup_phone_number: "+16505555555", 
      pickup_business_name:"Noodle Star",
      dropoff_address: `${req.body.street}, ${req.body.city}, ${req.body.zipcode}`,
      dropoff_phone_number: req.body.dropoff_phone_number, 
      dropoff_contact_given_name: req.body.dropoff_contact_given_name,
      dropoff_contact_family_name: req.body.dropoff_contact_family_name,
      order_value: req.body.order_value,
    });
    externalDeliveryId = response.data.external_delivery_id; 

    res.send(response); // Return the response in JSON format
    console.log("Quote" , response)

});

app.post("/create-delivery" , async (req, res) =>{
  if (!externalDeliveryId) {
    return res.status(400).send("No delivery ID found. Please request a delivery quote first.");
  }
  const client = new DoorDashClient({
    developer_id: process.env.DEVELOPER_ID,
    key_id: process.env.KEY_ID,
    signing_secret: process.env.SIGNING_SECRET,
  });


  const response = await client.deliveryQuoteAccept(externalDeliveryId);

  const clothingTotal = (response.data.order_value).toFixed(2)
  const feeTotal = (response.data.fee / 100).toFixed(2)
  const orderTotal = Number(clothingTotal) + Number(feeTotal)
  const trackingUrl = response.data.tracking_url;

  const data = {
    clothingTotal: clothingTotal,
    feeTotal: feeTotal,
    orderTotal:orderTotal
  };

  res.render("order2", {
  clothingTotal : data.clothingTotal,
  feeTotal : data.feeTotal,
  orderTotal : data.orderTotal,
  trackingUrl: trackingUrl,
  }
  );
  console.log("ACCEPT", response);
  
});
