import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('menuitem', { name: 'Order' }).click();
  await page
    .locator('div')
    .filter({
      hasText:
        /^Fried RiceStir-frying rice with eggs and seafood\.11\.99\$Add$/,
    })
    .getByRole('button')
    .click();
  await page.getByRole('button', { name: 'Checkout' }).click();
  await page.getByRole('button', { name: 'Delivery' }).click();
  await page.getByPlaceholder('Street').click();
  await page
    .getByPlaceholder('Street')
    .fill('11 Madison Ave, New York, NY 10010');
  await page.getByPlaceholder('City').click();
  await page.getByPlaceholder('City').fill('djdsjdsj');
  await page.getByPlaceholder('Zipcode').click();
  await page.getByPlaceholder('Zipcode').fill('sdkdsjdsj');
  await page.getByPlaceholder('First Name').click();
  await page.getByPlaceholder('First Name').fill('sdjsjdsdj');
  await page.getByPlaceholder('Last Name').click();
  await page.getByPlaceholder('Last Name').fill('sdjdsjds');
  await page.getByPlaceholder('Phone Number').click();
  await page.getByPlaceholder('Phone Number').fill('16505555555');
  await page.locator('#menu-items-container').click();
  await page.getByRole('checkbox').check();
  await page
    .getByText(
      'Payment Summary Subtotal: $11.00 Delivery Fee: $9.75 Order Total: $20.75 Place'
    )
    .click();
  await page.getByRole('button', { name: 'Place Order' }).click();
  const page1Promise = page.waitForEvent('popup');
  await page
    .getByRole('link', { name: 'Click here to track your order' })
    .click();
  const page1 = await page1Promise;
});
