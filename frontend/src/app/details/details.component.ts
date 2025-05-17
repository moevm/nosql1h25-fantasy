import {
  AfterViewInit,
  Component,
  computed,
  EventEmitter,
  inject,
  input,
  InputSignal,
  output,
  Output,
  OutputEmitterRef,
  signal,
} from '@angular/core';
import { Book } from '../data-access/book.service';
import { Series } from '../data-access/series.service';
import { Movie } from '../data-access/movie.service';
import { AsyncPipe, DecimalPipe, NgIf } from '@angular/common';
import {
  TuiAlertService,
  TuiButton,
  TuiDialog,
  TuiError,
  TuiTextfield,
  TuiTextfieldDirective,
} from '@taiga-ui/core';
import {
  FormArray,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  TuiComboBoxModule,
  TuiInputModule,
  TuiMultiSelectModule,
  TuiSelectModule,
  TuiTextfieldControllerModule,
} from '@taiga-ui/legacy';
import { TuiAutoFocus } from '@taiga-ui/cdk';
import { HttpClient } from '@angular/common/http';
import { rootActions } from '../store/root-store/root.actions';
import { Store } from '@ngrx/store';
import {
  TuiFieldErrorPipe,
  TuiFilterByInputPipe,
  TuiRating,
} from '@taiga-ui/kit';

@Component({
  selector: 'app-details',
  imports: [
    NgIf,
    TuiDialog,
    ReactiveFormsModule,
    TuiInputModule,
    TuiAutoFocus,
    TuiButton,
    TuiTextfieldControllerModule,
    TuiTextfieldDirective,
    TuiTextfield,
    TuiRating,
    FormsModule,
    DecimalPipe,
    TuiSelectModule,
    TuiComboBoxModule,
    TuiFilterByInputPipe,
    TuiMultiSelectModule,
    TuiError,
    TuiFieldErrorPipe,
    AsyncPipe,
  ],
  templateUrl: './details.component.html',
  styleUrl: './details.component.less',
  standalone: true,
})
export class DetailsComponent implements AfterViewInit {
  public inputObject: InputSignal<Book | Series | Movie | undefined> =
    input();

  @Output() back = new EventEmitter<void>();

  public updated: OutputEmitterRef<Book | Series | Movie> = output();

  protected open = false;

  private url = 'http://localhost:8080/catalog';

  private http = inject(HttpClient);

  private store = inject(Store);

  protected readonly alerts = inject(TuiAlertService);

  protected userRating = 8.5;

  protected readonly types = ['BOOK', 'SERIES', 'FILM'];

  protected readonly allowedTags = [
    'ACTION',
    'ADVENTURE',
    'FANTASY',
    'HORROR',
    'MYSTERY',
    'SCIENCE_FICTION',
  ];

  protected readonly countries = [
    'France',
    'Germany',
    'Japan',
    'UK',
    'USA',
  ];

  protected search: string | null = '';

  private localObject = signal<Book | Series | Movie | undefined>(
    undefined
  );

  protected object = computed(
    () => this.localObject() ?? this.inputObject()
  );

  protected editForm = new FormGroup({
    editTitle: new FormControl('', [
      Validators.required,
      Validators.maxLength(100),
    ]),
    editType: new FormControl('', [Validators.required]),
    editDescription: new FormControl('', [
      Validators.required,
      Validators.maxLength(1000),
    ]),
    editStartYear: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\d{4}$/),
      Validators.min(1800),
      Validators.max(2025),
    ]),
    editEndYear: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\d{4}$/),
      Validators.min(1800),
      Validators.max(2025),
    ]),
    editTags: new FormControl<string[]>([], [Validators.required]),
    editRating: new FormControl('', [
      Validators.required,
      Validators.min(0),
      Validators.max(10),
      Validators.pattern(/^\d+(\.\d{1,2})?$/),
    ]),
    editCountry: new FormControl('', [
      Validators.required,
      Validators.maxLength(50),
    ]),
    editQuantityPages: new FormControl(''),
    editDuration: new FormControl(''),
    editSeasons: new FormControl(''),
    editPersons: new FormArray([]),
  });

  ngAfterViewInit() {
    if (this.object()) {
      this.editForm.patchValue({
        editTitle: this.object()?.title ?? '',
        editType: this.object()?.type ?? '',
        editDescription: this.object()?.description ?? '',
        editStartYear: this.object()?.startYear.toString() ?? '',
        editEndYear: this.object()?.endYear.toString() ?? '',
        editTags: this.object()?.tags ?? [],
        editRating: this.object()?.rating.toString() ?? '',
        editCountry: this.object()?.country ?? '',
        editQuantityPages:
          this.getBook()?.quantityPages?.toString() ?? '',
        editDuration: this.getMovie()?.duration?.toString() ?? '',
        editSeasons: this.getSeries()?.seasons?.toString() ?? '',
      });

      this.populatePersons();
      this.setConditionalValidators();

      this.userRating = this.object()?.rating as number;
    }
  }

  setConditionalValidators(): void {
    this.editForm
      .get('editQuantityPages')
      ?.setValidators([
        ...(this.getBook() ? [Validators.required] : []),
        Validators.min(1),
        Validators.max(10000),
        Validators.pattern(/^\d+$/),
      ]);
    this.editForm.get('editQuantityPages')?.updateValueAndValidity();

    this.editForm
      .get('editDuration')
      ?.setValidators([
        ...(this.getMovie() ? [Validators.required] : []),
        Validators.min(1),
        Validators.max(1000),
        Validators.pattern(/^\d+$/),
      ]);
    this.editForm.get('editDuration')?.updateValueAndValidity();

    this.editForm
      .get('editSeasons')
      ?.setValidators([
        ...(this.getSeries() ? [Validators.required] : []),
        Validators.min(1),
        Validators.max(100),
        Validators.pattern(/^\d+$/),
      ]);
    this.editForm.get('editSeasons')?.updateValueAndValidity();
  }

  private populatePersons(): void {
    const persons = this.object()?.persons || [];
    const personsFormArray = this.editForm.get(
      'editPersons'
    ) as FormArray;
    persons.forEach(person => {
      personsFormArray.push(
        new FormGroup({
          role: new FormControl(person.role),
          name: new FormControl(person.name, [
            Validators.required,
            Validators.minLength(1),
          ]),
        })
      );
    });
  }

  getBook() {
    return this.object()?.type === 'BOOK'
      ? (this.object() as Book)
      : null;
  }

  getSeries() {
    return this.object()?.type === 'SERIES'
      ? (this.object() as Series)
      : null;
  }

  getMovie() {
    return this.object()?.type === 'FILM'
      ? (this.object() as Movie)
      : null;
  }

  goBack() {
    this.back.emit();
  }

  protected get editPersons(): FormArray {
    return this.editForm.get('editPersons') as FormArray;
  }

  protected showDialog(): void {
    this.ensureEssentialRoles();
    this.open = true;
  }

  private ensureEssentialRoles(): void {
    const currentRoles = this.editPersons.controls.map(
      ctrl => ctrl.get('role')?.value
    );
    for (const role of this.getEssentialRoles()) {
      if (!currentRoles.includes(role)) {
        this.addPerson(role);
      }
    }
  }

  private getEssentialRoles(): string[] {
    const type = this.object()?.type;
    switch (type) {
      case 'BOOK':
        return ['AUTHOR'];
      case 'FILM':
      case 'SERIES':
        return ['DIRECTOR', 'ACTOR'];
      default:
        return [];
    }
  }

  protected addPerson(role: string): void {
    const personGroup = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
      ]),
      role: new FormControl(role),
    });
    this.editPersons.push(personGroup);
  }

  protected removePerson(index: number): void {
    this.editPersons.removeAt(index);
  }

  protected submitEdit(): void {
    if (this.editForm.invalid) {
      this.alerts
        .open(`Форма не корректна`, {
          appearance: 'warning',
          autoClose: 5000,
        })
        .subscribe();
      return;
    }

    const personsArray = (
      this.editForm.get('editPersons') as FormArray
    ).value;

    const payload = {
      id: this.object()?.id,
      title: this.editForm.value.editTitle,
      type: this.editForm.value.editType,
      description: this.editForm.value.editDescription,
      startYear: Number(this.editForm.value.editStartYear),
      endYear: Number(this.editForm.value.editEndYear),
      tags: this.editForm.value.editTags ?? [],
      rating: Number(this.editForm.value.editRating),
      country: this.editForm.value.editCountry?.toUpperCase(),
      quantityPages: this.editForm.value.editQuantityPages
        ? Number(this.editForm.value.editQuantityPages)
        : undefined,
      duration: this.editForm.value.editDuration
        ? Number(this.editForm.value.editDuration)
        : undefined,
      seasons: this.editForm.value.editSeasons
        ? Number(this.editForm.value.editSeasons)
        : undefined,
      persons: personsArray,
      reviews: this.object()?.reviews,
    };

    this.http
      .put<
        Book | Series | Movie
      >(`${this.url}/${payload.id}`, payload)
      .subscribe({
        next: (updatedObject: Book | Series | Movie) => {
          this.localObject.set(updatedObject);
          this.store.dispatch(
            rootActions.objectUpdated({ updatedObject })
          );
          this.updated.emit(updatedObject);

          this.alerts
            .open(`Форма отправлена успешна`, {
              appearance: 'positive',
              autoClose: 5000,
            })
            .subscribe();
        },
        error: (err: unknown) => {
          console.error('Ошибка при отправке формы:', err);
        },
      });
  }

  protected rate(): void {
    const obj: Book | Series | Movie = JSON.parse(
      JSON.stringify(this.object())
    );
    if (obj) {
      obj.reviews.push({
        rating: this.userRating,
        reviewerName: '',
        text: '',
      });
      let sum = 0;
      obj.reviews.forEach(review => (sum += review.rating));
      obj.rating = sum / obj.reviews.length;
      this.http
        .put<Book | Series | Movie>(`${this.url}/${obj.id}`, obj)
        .subscribe({
          next: (updatedObject: Book | Series | Movie) => {
            this.localObject.set(updatedObject);
            this.store.dispatch(
              rootActions.objectUpdated({ updatedObject })
            );
            this.updated.emit(updatedObject);
            this.alerts
              .open(`Rating successfully added!`, {
                appearance: 'positive',
                autoClose: 5000,
              })
              .subscribe();
          },
          error: (err: unknown) => {
            console.error('Ошибка при отправке рейтинга:', err);
            this.alerts
              .open(`Error adding rating!`, {
                appearance: 'negative',
                autoClose: 5000,
              })
              .subscribe();
          },
        });
    }
  }
}
