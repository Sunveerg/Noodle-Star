import { test, expect } from '@playwright/test';

test('getAllReview', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Go to Reviews' }).click();
});