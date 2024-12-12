import { test, expect } from '@playwright/test';

test('addItemsIntoOrder', async ({ page }) => {
  await page.goto('http://localhost:3000/menuOrder');
  await page.locator('div').filter({ hasText: /^Fu PotA seafood pot\.15\.99\$Add$/ }).getByRole('button').click();
  await page.locator('div').filter({ hasText: /^Sichuan FishFish cooked in a hot sauce\.18\.99\$Add$/ }).getByRole('button').click();
  await page.locator('div').filter({ hasText: /^Sichuan FishFish cooked in a hot sauce\.18\.99\$Add$/ }).getByRole('button').click();
  await page.locator('div').filter({ hasText: /^Spring RollsA filling of vegetables\.4\.99\$Add$/ }).getByRole('button').click();
});