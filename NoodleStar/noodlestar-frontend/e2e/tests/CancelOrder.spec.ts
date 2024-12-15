import { test, expect } from '@playwright/test';

test('cancelOrder', async ({ page }) => {
  await page.goto('http://localhost:3000/menuOrder');
  await page.locator('div').filter({ hasText: /^Fried RiceStir-frying rice with eggs and seafood\.11\.99\$Add$/ }).getByRole('button').click();
  await page.locator('div').filter({ hasText: /^Won SoupDumplings filled with seasoned pork\.5\.99\$Add$/ }).getByRole('button').click();
  await page.locator('div').filter({ hasText: /^Won SoupDumplings filled with seasoned pork\.5\.99\$Add$/ }).getByRole('button').click();
  await page.getByRole('button', { name: 'Checkout' }).click();
  await page.getByRole('button', { name: 'Cancel Order' }).click();
  await page.goto('http://localhost:3000/menuOrder');
});
