import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('felix@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('FtCJwdC7c3ZiNeD!');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await page.getByLabel('Leave a review').click();
  await page.getByPlaceholder('Enter rating').click();
  await page.getByPlaceholder('Enter rating').fill('4');
  await page.getByPlaceholder('Enter your name').click();
  await page.getByPlaceholder('Enter your name').fill('felix zhang');
  await page.getByPlaceholder('Enter your review').click();
  await page.getByPlaceholder('Enter your review').fill('great service');
  await page.getByRole('button', { name: 'Submit Review' }).click();
  await expect(page.locator('div').filter({ hasText: /^★★★★★felix zhanggreat service2\/9\/2025❌✏️$/ }).first()).toBeVisible();
  await page.getByRole('button', { name: '✏️' }).nth(1).click();
  await page.getByRole('spinbutton').click();
  await page.getByRole('spinbutton').fill('5');
  await page.getByRole('button', { name: 'Update' }).click();
  await expect(page.locator('div').filter({ hasText: /^★★★★★felix zhanggreat service2\/9\/2025 • \(Edited\)❌✏️$/ }).first()).toBeVisible();
  await expect(page.getByText('• (Edited)').nth(1)).toBeVisible();
});