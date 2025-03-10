import { DoorDashClient } from "@doordash/sdk";
import "dotenv/config"

const client = new DoorDashClient({
    developer_id: process.env.DEVELOPER_ID,
    key_id: process.env.KEY_ID,
    signing_secret: process.env.SIGNING_SECRET
});

const response = client
  .getDelivery('95b528ea-4b1c-41a7-b52a-590cf4d45896')
  .then(response => {
    console.log(response.data)
  })
  .catch(err => {
    console.log(err)
  })

  console.log(response);