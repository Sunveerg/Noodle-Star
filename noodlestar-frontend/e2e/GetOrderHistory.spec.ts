import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('felix@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('FtCJwdC7c3ZiNeD!');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await expect(page.getByRole('heading', { name: 'Order History' })).toBeVisible();
  await expect(page.getByText('Order ID: orderId6Status:')).toBeVisible();
  await expect(page.getByText('Order ID: orderId7Status:')).toBeVisible();
});