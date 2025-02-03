import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('sam@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('Ronaldo07');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await expect(page.getByRole('button', { name: 'Modify' })).toBeVisible();
  await page.getByRole('button', { name: 'Modify' }).click();
  await page.locator('input[name="email"]').click();
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowLeft');
  await page.locator('input[name="email"]').press('ArrowRight');
  await page.locator('input[name="email"]').fill('sam27@gmail.com');
  await page.getByRole('button', { name: 'Update User' }).click();
  await expect(page.getByText('sam27@gmail.com')).toBeVisible();
});
