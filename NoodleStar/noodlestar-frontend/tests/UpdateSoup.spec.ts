import { test, expect } from '@playwright/test';

test('UpdateSoup', async ({ page }) => {
  await page.goto('http://localhost:3000/menu');
  await page.locator('.btn-edit').first().click();
  await page.getByPlaceholder('Enter dish name').click();
  await page.getByPlaceholder('Enter dish name').fill('Won Soup2');
  await page.getByRole('button', { name: 'Update' }).click();
});