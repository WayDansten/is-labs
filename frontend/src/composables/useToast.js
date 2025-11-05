import { useToast } from 'primevue'

export function useToastNotifier() {
  const toast = useToast()

  const bakeToast = (message, isSuccessful) => {
    if (isSuccessful) {
      toast.add({
        severity: 'success',
        summary: 'Success!',
        detail: message,
        life: 5000,
      })
    } else {
      toast.add({
        severity: 'error',
        summary: 'Error!',
        detail: message,
        life: 5000,
      })
    }
  }

  return { bakeToast }
}
