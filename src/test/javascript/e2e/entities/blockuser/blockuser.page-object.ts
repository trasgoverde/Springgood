import { element, by, ElementFinder } from 'protractor';

export class BlockuserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-blockuser div table .btn-danger'));
  title = element.all(by.css('jhi-blockuser div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class BlockuserUpdatePage {
  pageTitle = element(by.id('jhi-blockuser-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  creationDateInput = element(by.id('field_creationDate'));

  cblockeduserSelect = element(by.id('field_cblockeduser'));
  cblockinguserSelect = element(by.id('field_cblockinguser'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async cblockeduserSelectLastOption(): Promise<void> {
    await this.cblockeduserSelect.all(by.tagName('option')).last().click();
  }

  async cblockeduserSelectOption(option: string): Promise<void> {
    await this.cblockeduserSelect.sendKeys(option);
  }

  getCblockeduserSelect(): ElementFinder {
    return this.cblockeduserSelect;
  }

  async getCblockeduserSelectedOption(): Promise<string> {
    return await this.cblockeduserSelect.element(by.css('option:checked')).getText();
  }

  async cblockinguserSelectLastOption(): Promise<void> {
    await this.cblockinguserSelect.all(by.tagName('option')).last().click();
  }

  async cblockinguserSelectOption(option: string): Promise<void> {
    await this.cblockinguserSelect.sendKeys(option);
  }

  getCblockinguserSelect(): ElementFinder {
    return this.cblockinguserSelect;
  }

  async getCblockinguserSelectedOption(): Promise<string> {
    return await this.cblockinguserSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class BlockuserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-blockuser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-blockuser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
