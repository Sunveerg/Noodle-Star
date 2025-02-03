import { test, expect } from '@playwright/test';

test('viewReport', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('felix@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('FtCJwdC7c3ZiNeD!');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await expect(page.getByRole('button', { name: 'Report' })).toBeVisible();
  await page.getByRole('button', { name: 'Report' }).click();
  await expect(
    page.getByRole('heading', { name: 'Menu Item Order Frequency' })
  ).toBeVisible();
});
