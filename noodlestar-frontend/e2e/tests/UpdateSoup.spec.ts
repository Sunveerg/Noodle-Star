import { test, expect } from '@playwright/test';

test('UpdateSoup', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('zako@gmail.com');
  await page.getByLabel('Email address').press('Tab');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('Ronaldo07');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await expect(page.getByText('Welcome back, zako')).toBeVisible();
  await page.goto('http://localhost:3000/menu');
  await page.locator('.btn-edit').first().click();
  await page.getByPlaceholder('Enter dish name').click();
  await page.getByPlaceholder('Enter dish name').fill('Won Soup2');
  await page.getByRole('button', { name: 'Update' }).click();
});
