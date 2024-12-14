import { test, expect } from '@playwright/test';

test('OrderApi', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('menuitem', { name: 'Order' }).click();
  await page.getByRole('button', { name: 'Checkout' }).click();
  await page.getByPlaceholder('Street').click();
  await page.getByPlaceholder('Street').fill('11 Madison Ave, New York, NY 10010');
  await page.getByPlaceholder('City').click();
  await page.getByPlaceholder('City').fill('hubert');
  await page.getByPlaceholder('Zipcode').click();
  await page.getByPlaceholder('Zipcode').fill('jurtds');
  await page.getByPlaceholder('First Name').click();
  await page.getByPlaceholder('First Name').fill('jonhn');
  await page.getByPlaceholder('Last Name').click();
  await page.getByPlaceholder('Last Name').fill('doe');
  await page.getByPlaceholder('Phone Number').click();
  await page.getByPlaceholder('Phone Number').fill('16505555555');
  await page.locator('#menu-items-container div').filter({ hasText: 'Fu Pot - $' }).getByRole('checkbox').check();
  await page.getByText('Payment Summary Subtotal: $15').click();
  await page.getByRole('button', { name: 'Place Order' }).click();
  const page1Promise = page.waitForEvent('popup');
  await page.getByRole('link', { name: 'Click here to track your order' }).click();
  const page1 = await page1Promise;
});