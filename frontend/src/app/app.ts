
import {
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormsModule,
} from '@angular/forms';

import { finalize } from 'rxjs';

import {
  InternService,
  Batch,
  Intern,
  CreateInternRequest,
  UpdateInternRequest,
  BatchDetails,
} from './services/intern.service';

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  currentPage = 'Dashboard';

  showInternForm = false;
  showBatchForm = false;

  // Edit mode
  editingInternId: number | null = null;
  editingInternCode = '';

  isSubmitting = false;
  isLoadingBatches = false;
  isLoadingInterns = false;
  isCreatingBatch = false;
  isLoadingBatchDetails = false;

  errorMessage = '';
  successMessage = '';
  recordsError = '';
  batchError = '';
  batchSuccess = '';
  batchRecordsError = '';

  batches: Batch[] = [];
  interns: Intern[] = [];

  searchName = '';
  filterBatchId = '';
  filterIdCardType = '';

  internForm: FormGroup;
  batchForm: FormGroup;

  selectedBatchId: number | null = null;
  selectedBatchDetails: BatchDetails | null = null;

  constructor(
    private fb: FormBuilder,
    private internService: InternService,
    private cdr: ChangeDetectorRef,
  ) {
    this.internForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[6-9]\d{9}$/),
        ],
      ],
      idCardType: ['FREE', Validators.required],
      dateOfJoining: ['', Validators.required],
      batchId: ['', [Validators.required, Validators.min(1)]],
    });

    this.batchForm = this.fb.group({
      startDate: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadBatches();
    this.loadInterns();
  }

  // =========================================
  // LOAD BATCHES
  // =========================================

  loadBatches(): void {
    this.isLoadingBatches = true;
    this.batchRecordsError = '';
    this.cdr.markForCheck();

    this.internService
      .getBatches()
      .pipe(
        finalize(() => {
          this.isLoadingBatches = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (batches) => {
          this.batches = Array.isArray(batches) ? batches : [];
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to load batches:', error);

          this.errorMessage = 'Unable to load batches. Check the backend connection.';
          this.batchRecordsError = 'Unable to load batches. Please try again.';

          this.cdr.markForCheck();
        },
      });
  }

  // =========================================
  // LOAD INTERNS
  // =========================================

  loadInterns(): void {
    this.isLoadingInterns = true;
    this.recordsError = '';
    this.cdr.markForCheck();

    this.internService
      .getInterns()
      .pipe(
        finalize(() => {
          this.isLoadingInterns = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (response) => {
          this.interns = Array.isArray(response) ? response : [];
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to load interns:', error);

          this.recordsError =
            error.status === 0
              ? 'Cannot connect to the backend. Check that Spring Boot is running.'
              : `Failed to load interns (HTTP ${error.status}).`;

          this.cdr.markForCheck();
        },
      });
  }

  // =========================================
  // NAVIGATION
  // =========================================

  navigateTo(page: string): void {
    this.currentPage = page;

    this.resetInternForm();
    this.showBatchForm = false;

    this.errorMessage = '';
    this.successMessage = '';
    this.recordsError = '';
    this.batchError = '';
    this.batchSuccess = '';
    this.batchRecordsError = '';

    if (page === 'Interns') {
      this.loadInterns();
      this.loadBatches();
    }

    if (page === 'Batches') {
      this.loadBatches();
      this.closeBatchDetails();
    }

    this.cdr.markForCheck();
  }

  // =========================================
  // INTERN FORM
  // =========================================

  toggleInternForm(): void {
    if (this.showInternForm) {
      this.resetInternForm();
      return;
    }

    this.resetInternForm();
    this.showInternForm = true;
    this.loadBatches();
    this.cdr.markForCheck();
  }

  private resetInternForm(): void {
    this.internForm.enable();

    this.internForm.reset({
      name: '',
      email: '',
      mobileNumber: '',
      idCardType: 'FREE',
      dateOfJoining: '',
      batchId: '',
    });

    this.editingInternId = null;
    this.editingInternCode = '';
    this.showInternForm = false;
    this.errorMessage = '';
    this.successMessage = '';

    this.cdr.markForCheck();
  }

  // =========================================
  // EDIT INTERN
  // =========================================

  editIntern(intern: Intern): void {
    if (intern.id == null) {
      this.recordsError =
        'Cannot edit this record because its database ID is missing. Update the backend InternResponse to include the numeric id.';
      this.cdr.markForCheck();
      return;
    }

    this.editingInternId = intern.id;
    this.editingInternCode = intern.internId;

    this.errorMessage = '';
    this.successMessage = '';
    this.recordsError = '';

    this.internForm.enable();

    this.internForm.reset({
      name: intern.name,
      email: intern.email,
      mobileNumber: intern.mobileNumber,
      idCardType: intern.idCardType,
      dateOfJoining: intern.dateOfJoining,
      batchId: String(intern.batchId),
    });

    // The backend update endpoint changes only these three fields.
    // Keep the generated ID, card type, joining date and batch unchanged.
    this.internForm.get('idCardType')?.disable();
    this.internForm.get('dateOfJoining')?.disable();
    this.internForm.get('batchId')?.disable();

    this.showInternForm = true;

    this.loadBatches();

    // Scroll the existing form into view.
    setTimeout(() => {
      document.querySelector('.registration-card')?.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
      });
    }, 0);

    this.cdr.markForCheck();
  }

  // =========================================
  // REGISTER OR UPDATE INTERN
  // =========================================

  submitInternForm(): void {
    if (this.internForm.invalid || this.isSubmitting) {
      this.internForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const value = this.internForm.getRawValue();

    if (this.editingInternId !== null) {
      const request: UpdateInternRequest = {
        name: String(value.name).trim(),
        email: String(value.email).trim(),
        mobileNumber: String(value.mobileNumber).trim(),
      };

      this.internService
        .updateIntern(this.editingInternId, request)
        .pipe(
          finalize(() => {
            this.isSubmitting = false;
            this.cdr.markForCheck();
          }),
        )
        .subscribe({
          next: () => {
  const internCode = this.editingInternCode;

  this.resetInternForm();

  this.successMessage =
    `Intern ${internCode} updated successfully!`;

  this.loadInterns();
  this.loadBatches();

            if (this.selectedBatchId !== null) {
              this.viewBatchDetails(this.selectedBatchId);
            }

            this.cdr.markForCheck();
          },
          error: (error) => {
            console.error('Failed to update intern:', error);

            this.errorMessage =
              error.error?.message ??
              (error.status === 0
                ? 'Cannot connect to the backend.'
                : `Failed to update intern (HTTP ${error.status}).`);

            this.cdr.markForCheck();
          },
        });

      return;
    }

    const request: CreateInternRequest = {
      name: String(value.name).trim(),
      email: String(value.email).trim(),
      mobileNumber: String(value.mobileNumber).trim(),
      idCardType: value.idCardType,
      dateOfJoining: value.dateOfJoining,
      batchId: Number(value.batchId),
    };

    this.internService
      .registerIntern(request)
      .pipe(
        finalize(() => {
          this.isSubmitting = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (response) => {
          const internCode = response.internId;

          this.resetInternForm();

          // Show success after closing/resetting the form.
          this.successMessage =
            `Intern registered successfully! ID: ${internCode}`;

          this.loadInterns();
          this.loadBatches();

          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to register intern:', error);

          if (error.status === 0) {
            this.errorMessage =
              'Cannot connect to the backend. Check Spring Boot and CORS.';
          } else if (error.status === 400) {
            this.errorMessage =
              error.error?.message ??
              'Check the details, selected batch, and joining date.';
          } else if (error.status === 409) {
            this.errorMessage =
              error.error?.message ??
              'An intern with these details may already exist.';
          } else {
            this.errorMessage =
              error.error?.message ??
              `Registration failed (HTTP ${error.status}).`;
          }

          this.cdr.markForCheck();
        },
      });
  }

  // =========================================
  // DELETE INTERN
  // =========================================

  deleteIntern(intern: Intern): void {
    if (intern.id == null) {
      this.recordsError =
        'Cannot delete this record because its database ID is missing. Update the backend InternResponse to include the numeric id.';
      this.cdr.markForCheck();
      return;
    }

    const confirmed = window.confirm(
      `Are you sure you want to delete ${intern.name} (${intern.internId})? This action cannot be undone.`,
    );

    if (!confirmed) {
      return;
    }

    this.recordsError = '';
    this.successMessage = '';

    this.internService
      .deleteIntern(intern.id)
      .subscribe({
        next: () => {
          this.interns = this.interns.filter(
            (item) => item.id !== intern.id,
          );

          this.successMessage =
            `Intern ${intern.internId} deleted successfully.`;

          this.loadInterns();
          this.loadBatches();

          if (this.selectedBatchId !== null) {
            this.viewBatchDetails(this.selectedBatchId);
          }

          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to delete intern:', error);

          this.recordsError =
            error.error?.message ??
            (error.status === 0
              ? 'Cannot connect to the backend.'
              : `Failed to delete intern (HTTP ${error.status}).`);

          this.cdr.markForCheck();
        },
      });
  }

  // =========================================
  // BATCH FORM
  // =========================================

  toggleBatchForm(): void {
    this.showBatchForm = !this.showBatchForm;
    this.batchError = '';
    this.batchSuccess = '';
    this.cdr.markForCheck();
  }

  // =========================================
  // CREATE BATCH
  // =========================================

  createBatch(): void {
    if (this.batchForm.invalid || this.isCreatingBatch) {
      this.batchForm.markAllAsTouched();
      return;
    }

    this.isCreatingBatch = true;
    this.batchError = '';
    this.batchSuccess = '';

    const startDate = this.batchForm.get('startDate')?.value;

    this.internService
      .createBatch({ startDate })
      .pipe(
        finalize(() => {
          this.isCreatingBatch = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (batch) => {
          this.batchSuccess =
            `Batch #${batch.id} created successfully!`;

          this.batchForm.reset({ startDate: '' });
          this.loadBatches();

          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to create batch:', error);

          this.batchError =
            error.error?.message ??
            `Unable to create batch (HTTP ${error.status}).`;

          this.cdr.markForCheck();
        },
      });
  }

  // =========================================
  // VIEW BATCH DETAILS
  // =========================================

  viewBatchDetails(batchId: number): void {
    this.selectedBatchId = batchId;
    this.selectedBatchDetails = null;
    this.batchRecordsError = '';
    this.isLoadingBatchDetails = true;
    this.cdr.markForCheck();

    this.internService
      .getBatchDetails(batchId)
      .pipe(
        finalize(() => {
          this.isLoadingBatchDetails = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (details) => {
          this.selectedBatchDetails = details;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Failed to load batch details:', error);

          this.batchRecordsError =
            error.error?.message ??
            `Unable to load batch details (HTTP ${error.status}).`;

          this.cdr.markForCheck();
        },
      });
  }

  closeBatchDetails(): void {
    this.selectedBatchId = null;
    this.selectedBatchDetails = null;
    this.isLoadingBatchDetails = false;
    this.batchRecordsError = '';
    this.cdr.markForCheck();
  }

  // =========================================
  // FILTER INTERN RECORDS
  // =========================================

  get filteredInterns(): Intern[] {
    const name = this.searchName.trim().toLowerCase();

    return this.interns.filter((intern) => {
      const matchesName =
        !name ||
        (intern.name ?? '').toLowerCase().includes(name) ||
        (intern.internId ?? '').toLowerCase().includes(name) ||
        (intern.email ?? '').toLowerCase().includes(name);

      const matchesBatch =
        !this.filterBatchId ||
        Number(intern.batchId) === Number(this.filterBatchId);

      const matchesType =
        !this.filterIdCardType ||
        intern.idCardType === this.filterIdCardType;

      return matchesName && matchesBatch && matchesType;
    });
  }

  clearFilters(): void {
    this.searchName = '';
    this.filterBatchId = '';
    this.filterIdCardType = '';
    this.cdr.markForCheck();
  }

  // =========================================
  // DASHBOARD STATISTICS
  // =========================================

  get totalInterns(): number {
    return this.interns.length;
  }

  get premiumInterns(): number {
    return this.interns.filter(
      (intern) => intern.idCardType === 'PREMIUM',
    ).length;
  }

  get freeInterns(): number {
    return this.interns.filter(
      (intern) => intern.idCardType === 'FREE',
    ).length;
  }

  get totalBatches(): number {
    return this.batches.length;
  }

  getCardCount(type: 'FREE' | 'PREMIUM'): number {
    return this.interns.filter(
      (intern) => intern.idCardType === type,
    ).length;
  }
}
