import { test, expect } from '@playwright/test';

test('LoginFailure', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('hamid@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('Ronaldo07');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await expect(page.getByText('Wrong email or password')).toBeVisible();
});