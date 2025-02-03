import { test, expect } from '@playwright/test';

test('AddPiza', async ({ page }) => {
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
  await page.getByRole('button', { name: '+' }).click();
  await page.locator('input[name="name"]').click();
  await page.locator('input[name="name"]').fill('pizza');
  await page.locator('textarea[name="description"]').click();
  await page.locator('textarea[name="description"]').fill('very good');
  await page.getByRole('spinbutton').click();
  await page.getByRole('spinbutton').fill('');
  await page.locator('input[name="price"]').click();
  await page.locator('input[name="price"]').fill('200');
  await page.locator('input[name="category"]').click();
  await page.locator('input[name="category"]').fill('dinner');
  await page.locator('input[name="itemImage"]').click();
  await page.locator('input[name="itemImage"]').fill('pizza.jpg');
  await page.getByRole('button', { name: 'Add Dish' }).click();
});
