import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/menuOrder');
  await page
    .locator('div')
    .filter({ hasText: /^Spring RollsA filling of vegetables\.4\.99\$Add$/ })
    .getByRole('button')
    .click();
  await page
    .locator('div')
    .filter({
      hasText: /^Beef and BroccoliChinese stir-fry dish\.13\.49\$Add$/,
    })
    .getByRole('button')
    .click();
  await page.getByRole('button', { name: 'Checkout' }).click();
  await expect(
    page.getByText(
      'Order DetailsProductTotalSpring Rolls x1$4.99Beef and Broccoli x1$13.49Subtotal'
    )
  ).toBeVisible();
  await page.getByRole('cell', { name: 'Spring Rolls x1' }).click();
  await page.getByRole('cell', { name: 'Beef and Broccoli x1' }).click();
});
