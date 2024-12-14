import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
    await page.goto('http://localhost:3000/home');
    await page.getByRole('button', { name: 'Go to Reviews' }).click();
    await page.getByRole('button', { name: 'Add' }).click();
    await page.getByPlaceholder('Enter rating').click();
    await page.getByPlaceholder('Enter rating').fill('10');
    await page.getByPlaceholder('Enter your name').fill('Sunveer');
    await page.getByPlaceholder('Enter your review').click();
    await page.getByPlaceholder('Enter your review').fill('Amazing!');
    await page.getByRole('button', { name: 'Submit Review' }).click();
    await expect(page.getByText('Rating must be between 1 and')).toBeVisible();
});
