import { test, expect } from '@playwright/test';

test('deleteReview', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').fill('zako@gmail.com');
  await page.getByLabel('Email address').press('Tab');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('R');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('Ronaldo07');
  await page.getByLabel('Password').press('Enter');
  await page.getByLabel('Leave a review').click();
  await page.getByPlaceholder('Enter rating').click();
  await page.getByPlaceholder('Enter rating').fill('1');
  await page.getByPlaceholder('Enter your name').click();
  await page.getByPlaceholder('Enter your name').press('CapsLock');
  await page.getByPlaceholder('Enter your name').fill('H');
  await page.getByPlaceholder('Enter your name').press('CapsLock');
  await page.getByPlaceholder('Enter your name').fill('Has');
  await page.getByPlaceholder('Enter your name').press('Tab');
  await page.getByPlaceholder('Enter your review').fill('shdad');
  await page.getByRole('button', { name: 'Submit Review' }).click();
  await page.getByRole('button', { name: 'Go to Reviews' }).click();
  await expect(page.locator('div:nth-child(31) > .review-item-content > .delete-btn')).toBeVisible();
  await page.locator('div:nth-child(31) > .review-item-content > .delete-btn').click();
});