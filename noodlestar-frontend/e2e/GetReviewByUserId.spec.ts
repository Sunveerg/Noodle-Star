import { test, expect } from '@playwright/test';

test('GetReviewForAuser', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').fill('s');
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('sam@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('Ronaldo07');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await page.getByLabel('Leave a review').click();
  await page.getByPlaceholder('Enter rating').click();
  await page.getByPlaceholder('Enter rating').fill('3');
  await page.getByPlaceholder('Enter your name').click();
  await page.getByPlaceholder('Enter your name').fill('Sam');
  await page.getByPlaceholder('Enter your review').click();
  await page.getByPlaceholder('Enter your review').fill('meh');
  await page.getByPlaceholder('Enter your review').click();
  await page.getByRole('button', { name: 'Submit Review' }).click();
  await expect(page.getByRole('heading', { name: 'Your Reviews' })).toBeVisible();
});