import { test, expect } from '@playwright/test';

test('emptyOrder', async ({ page }) => {
  await page.goto('http://localhost:3000/menuOrder');
  await page.getByRole('button', { name: 'Checkout' }).click();
  await expect(page.getByText('Your cart is empty. Please')).toBeVisible();
});