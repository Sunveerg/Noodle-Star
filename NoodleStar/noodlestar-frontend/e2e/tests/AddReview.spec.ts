import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
    await page.goto('http://localhost:3000/home');
    await page.getByRole('button', { name: 'Go to Reviews' }).click();
    await page.getByRole('button', { name: 'Add' }).click();
    await page.getByPlaceholder('Enter rating').click();
    await page.getByPlaceholder('Enter rating').fill('5');
    await page.getByPlaceholder('Enter your name').click();
    await page.getByPlaceholder('Enter your name').fill('Sunveer');
    await page.getByPlaceholder('Enter your review').click();
    await page.getByPlaceholder('Enter your review').fill('Very Good Service!');
    await page.getByRole('button', { name: 'Submit Review' }).click();
    await page.goto('http://localhost:3000/review');
    await expect(page.locator('div').filter({ hasText: /^★★★★★SunveerVery Good Service!12\/14\/2024$/ }).first()).toBeVisible();
});
